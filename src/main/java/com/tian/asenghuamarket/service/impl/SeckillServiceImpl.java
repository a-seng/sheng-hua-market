package com.tian.asenghuamarket.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.tian.asenghuamarket.Dto.Seckill;
import com.tian.asenghuamarket.Dto.SeckillSuccess;
import com.tian.asenghuamarket.common.Constants;
import com.tian.asenghuamarket.common.SeckillStatusEnum;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.controller.vo.ExposerVO;
import com.tian.asenghuamarket.controller.vo.SeckillGoodsVO;
import com.tian.asenghuamarket.controller.vo.SeckillSuccessVO;
import com.tian.asenghuamarket.exception.AsengHuaMarketException;
import com.tian.asenghuamarket.mapper.GoodsInfoMapper;
import com.tian.asenghuamarket.mapper.SeckillMapper;
import com.tian.asenghuamarket.mapper.SeckillSuccessMapper;
import com.tian.asenghuamarket.redis.RedisCache;
import com.tian.asenghuamarket.service.SeckillService;
import com.tian.asenghuamarket.util.MD5Util;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;
import org.apache.ibatis.util.MapUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.MapUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SeckillServiceImpl implements SeckillService {

    //使用令牌桶RateLimiter限流
    private static final RateLimiter RATE_LIMITER=RateLimiter.create(100);

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private SeckillSuccessMapper seckillSuccessMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private RedisCache redisCache;


    @Override
    public PageResult getSeckillPage(PageQueryUtil pageUtil) {
        List<Seckill>carousels=seckillMapper.findSeckillList(pageUtil);
        int total=seckillMapper.getTotalSeckills(pageUtil);
        return new PageResult(carousels,total,pageUtil.getLimit(),pageUtil.getPage());
    }

    @Override
    public boolean saveSeckill(Seckill seckill) {
        if(goodsInfoMapper.selectById(seckill.getGoodsId())==null){
            AsengHuaMarketException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        return seckillMapper.insert(seckill)>0;
    }

    @Override
    public boolean updateSeckill(Seckill seckill) {
        if(goodsInfoMapper.selectById(seckill.getGoodsId())==null){
            AsengHuaMarketException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        Seckill seckill1 = seckillMapper.selectById(seckill.getSeckillId());
        if(seckill1==null){
            AsengHuaMarketException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        seckill.setUpdateTime(new Date());
        return seckillMapper.updateById(seckill)>0;
    }

    @Override
    public Seckill getSeckillById(Long id) {
        return seckillMapper.selectById(id);
    }

    @Override
    public boolean deleteSeckillById(Long id) {
        return seckillMapper.deleteById(id)>0;
    }

    @Override
    public List<Seckill> getHomeSeckillPage() {
        return seckillMapper.findHomeSeckillList();
    }

    @Override
    public ExposerVO exposerUrl(Long seckillId) {
        SeckillGoodsVO seckillGoodsVO = redisCache.getCacheObject(Constants.SECKILL_GOODS_DETAIL + seckillId);
        Date startTime = seckillGoodsVO.getSeckillBegin();
        Date endTime = seckillGoodsVO.getSeckillEnd();
        //系统当前时间
        Date nowTime=new Date();
        if(nowTime.getTime()<startTime.getTime() || nowTime.getTime()>endTime.getTime()){
            return new ExposerVO(SeckillStatusEnum.NOT_START,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //检查虚拟库存
        Integer stock = redisCache.getCacheObject(Constants.SECKILL_GOODS_STOCK_KEY + seckillId);
        if(stock==null || stock<0){
            return new ExposerVO(SeckillStatusEnum.STARTED_SHORTAGE_STOCK,seckillId);
        }
        //加密
        String md5=MD5Util.MD5Encode(seckillId.toString(),Constants.UTF_ENCODING);
        return new ExposerVO(SeckillStatusEnum.START,md5,seckillId);
    }

    @Override
    public SeckillSuccessVO executeSeckill(Long seckillId, Long userId) {
        //判断能否在500毫秒内得到令牌，如果补鞥呢则立即返回false，不会阻塞程序
        if(!RATE_LIMITER.tryAcquire(500, TimeUnit.MICROSECONDS)){
            throw new AsengHuaMarketException("秒杀失败");
        }
        //判断用户是否已经购买过秒杀商品
        if(redisCache.containsCacheSet(Constants.SECKILL_SUCCESS_USER_ID+seckillId,userId)){
            throw new AsengHuaMarketException("您已经购买过秒杀商品，请勿重复购买");
        }

        //更新秒杀商品虚拟库存
        Long stock = redisCache.luaDecrement(Constants.SECKILL_GOODS_STOCK_KEY + seckillId);
        if(stock<0){
            throw new AsengHuaMarketException("秒杀商品已售空")
        }
        Seckill seckill = redisCache.getCacheObject(Constants.SECKILL_KEY + seckillId);
        if(seckill==null){
            seckill = seckillMapper.selectById(seckillId);
            redisCache.setCacheObject(Constants.SECKILL_KEY+seckillId,seckill,24,TimeUnit.HOURS);
        }
        //判断秒杀商品是否在有效期内
        long beginTime = seckill.getSeckillBegin().getTime();
        long endTime = seckill.getSeckillEnd().getTime();
        Date now=new Date();
        long nowTIme=now.getTime();
        if(nowTIme<beginTime){
            throw new AsengHuaMarketException("秒杀未开启");
        }else if(nowTIme>endTime){
            throw new AsengHuaMarketException("秒杀已结束");
        }

        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>(8);
        map.put("seckillId",seckillId);
        map.put("userId",userId);
        map.put("killTime",killTime);
        map.put("result",null);
        //执行存储过程，result被赋值
        try {
            seckillMapper.killByProcedure(map);
        }catch (Exception e){
            throw new AsengHuaMarketException(e.getMessage());
        }
        // 获取result -2sql执行失败 -1 未插入数据 0未更新数据 1 sql执行成功
        map.get("result");
        int result = MapUtils.getInteger(map,"result",-2);
        if(result!=1){
            throw new AsengHuaMarketException("很遗憾！未抢购到秒杀商品");
        }
        //记录购买过的用户
        redisCache.setCacheSet(Constants.SECKILL_SUCCESS_USER_ID+seckillId,userId);
        long endExpireTime = endTime/1000;
        long nowExpireTime=nowTIme/1000;
        redisCache.expire(Constants.SECKILL_SUCCESS_USER_ID+seckillId,
                endExpireTime-nowExpireTime,TimeUnit.SECONDS);
        SeckillSuccess seckillSuccess=seckillMapper.getSeckillSuccessByUserIdAndSeckillId(userId,seckillId);
        SeckillSuccessVO seckillSuccessVO = new SeckillSuccessVO();
        Long seckillSuccessId = seckillSuccess.getSecId();
        seckillSuccessVO.setSeckillSuccessId(seckillSuccessId);
        seckillSuccessVO.setMd5(MD5Util.MD5Encode(seckillSuccessId+Constants.SECKILL_ORDER_SALT,Constants.UTF_ENCODING));
        return seckillSuccessVO;
    }
}
