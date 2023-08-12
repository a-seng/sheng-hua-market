package com.tian.asenghuamarket.service.impl;

import com.tian.asenghuamarket.Dto.GoodsInfo;
import com.tian.asenghuamarket.Dto.IndexConfig;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.controller.vo.IndexConfigGoodsVO;
import com.tian.asenghuamarket.mapper.GoodsInfoMapper;
import com.tian.asenghuamarket.mapper.IndexConfigMapper;
import com.tian.asenghuamarket.service.IndexConfigService;
import com.tian.asenghuamarket.util.BeanUtil;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;


    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigList = indexConfigMapper.findIndexConfigList(pageUtil);
        int totalIndexConfigs = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        return new PageResult(indexConfigList,totalIndexConfigs,pageUtil.getLimit(),pageUtil.getPage());
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        if(goodsInfoMapper.selectById(indexConfig.getGoodsId())==null){
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        if(indexConfigMapper.selectByTypeAndGoodsId(indexConfig.getConfigType(),indexConfig.getGoodsId())!=null){
            return ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult();
        }
        if(indexConfigMapper.insertSelective(indexConfig)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        if(goodsInfoMapper.selectById(indexConfig.getGoodsId())==null){
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(Long.valueOf(indexConfig.getConfigId()));
        if(temp == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        IndexConfig temp2 = indexConfigMapper.selectByTypeAndGoodsId(indexConfig.getConfigType(), indexConfig.getGoodsId());
        if(temp2!=null && !temp2.getConfigId().equals(indexConfig.getConfigId())){
            //goodsId相同且不同id，不能继续修改
            return ServiceResultEnum.SAME_INDEX_CONFIG_EXIST.getResult();
        }
        indexConfig.setUpdateTime(new Date());
        if(indexConfigMapper.updateByPrimaryKeySelective(indexConfig)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<IndexConfigGoodsVO> indexConfigGoodsVOS =new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if(!CollectionUtils.isEmpty(indexConfigs)){
            //取出所有的goodsId
            List<Integer> goodsIds = indexConfigs.stream().map(IndexConfig::getConfigId).collect(Collectors.toList());
            List<GoodsInfo> Goods = null;
            for (Integer goodsId : goodsIds) {
                Goods.add(goodsInfoMapper.selectById(goodsId));
            }
            indexConfigGoodsVOS = BeanUtil.copyList(Goods, IndexConfigGoodsVO.class);
            for (IndexConfigGoodsVO indexConfigGoodsVo : indexConfigGoodsVOS) {
                String goodsName = indexConfigGoodsVo.getGoodsName();
                String goodsIntro = indexConfigGoodsVo.getGoodsIntro();
                //字符串过长导致文字超出的问题
                if(goodsName.length()>30){
                    goodsName=goodsName.substring(0,30)+"...";
                    indexConfigGoodsVo.setGoodsName(goodsName);
                }
                if(goodsIntro.length()>22){
                    goodsIntro=goodsIntro.substring(0,22)+"...";
                    indexConfigGoodsVo.setGoodsIntro(goodsIntro);
                }
            }
        }
        return indexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if(ids.length<1){
            return false;
        }
        //删除数据
        return indexConfigMapper.deleteBatch(ids)>0;
    }
}
