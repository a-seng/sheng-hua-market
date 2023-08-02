package com.tian.asenghuamarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tian.asenghuamarket.Dto.GoodsCategory;
import com.tian.asenghuamarket.Dto.GoodsInfo;
import com.tian.asenghuamarket.common.CategoryLevelEnum;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.exception.AsengHuaMarketException;
import com.tian.asenghuamarket.mapper.GoodsCategoryMapper;
import com.tian.asenghuamarket.mapper.GoodsInfoMapper;
import com.tian.asenghuamarket.service.GoodsService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil) {

        return null;
    }

    @Override
    public String saveNewBeeMallGoods(GoodsInfo goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectById(goods.getGoodsId());
        //分类不存在或者不是三级分类,则该参数字段异常
        if(goodsCategory == null || goodsCategory.getCategoryLevel().intValue() != CategoryLevelEnum.LEVEL_THREE.getLevel()){
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        QueryWrapper<GoodsInfo> goodsInfoQueryWrapper = new QueryWrapper<>();
        goodsInfoQueryWrapper.eq("goods_name",goods.getGoodsName());
        goodsInfoQueryWrapper.eq("goods_category_id",goods.getGoodsCategoryId());
        if(goodsInfoMapper.selectOne(goodsInfoQueryWrapper)!=null){
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();

        }
        if(goodsInfoMapper.insert(goods)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveGoodsInfo(List<GoodsInfo> goodsInfoList) {
        if(!CollectionUtils.isEmpty(goodsInfoList)){
            for (GoodsInfo goodsInfo : goodsInfoList) {
                goodsInfoMapper.insert(goodsInfo);
            }
        }
    }

    @Override
    public String updateGoodsInfo(GoodsInfo goods) {
        GoodsCategory goodsCategory = goodsCategoryMapper.selectById(goods.getGoodsCategoryId());
        //分类不存在或者不是三级分裂，则该参数字段异常
        if(goodsCategory == null || goodsCategory.getCategoryLevel().intValue()!= CategoryLevelEnum.LEVEL_THREE.getLevel()){
            return ServiceResultEnum.GOODS_CATEGORY_ERROR.getResult();
        }
        GoodsInfo goodsInfo = goodsInfoMapper.selectById(goods.getGoodsId());
        if(goodsInfo==null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        QueryWrapper<GoodsInfo> goodsInfoQueryWrapper = new QueryWrapper<>();
        goodsInfoQueryWrapper.eq("category_id",goods.getGoodsName());
        goodsInfoQueryWrapper.eq("goods_name",goods.getGoodsName());
        GoodsInfo goodsInfo1 = goodsInfoMapper.selectOne(goodsInfoQueryWrapper);
        if(goodsInfo1!=null && !goodsInfo1.getGoodsId().equals(goods.getGoodsId())){
            //name和分类id相同且不同id不能继续修改
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        if(goodsInfoMapper.updateById(goods)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public GoodsInfo getGoodsInfoById(Long id) {
        GoodsInfo goodsInfo = goodsInfoMapper.selectById(id);
        if(goodsInfo == null){
            AsengHuaMarketException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        return goodsInfo;
    }

    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {

        return null;
    }

    @Override
    public PageResult searchGoodsInfo(PageQueryUtil pageUtil) {
        return null;
    }
}
