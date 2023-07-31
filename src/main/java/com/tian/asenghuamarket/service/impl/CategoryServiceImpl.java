package com.tian.asenghuamarket.service.impl;

import com.tian.asenghuamarket.Dto.GoodsCategory;
import com.tian.asenghuamarket.common.ServiceResultEnum;
import com.tian.asenghuamarket.mapper.GoodsCategoryMapper;
import com.tian.asenghuamarket.service.CategoryService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.yaml.snakeyaml.events.Event;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public String updateGoodsCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = goodsCategoryMapper.selectById(goodsCategory.getCategoryId());
        if(temp==null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("category_lever",goodsCategory.getCategoryLevel());
        map.put("category_name",goodsCategory.getCategoryName());
        GoodsCategory temp2= (GoodsCategory) goodsCategoryMapper.selectByMap(map);
        if(temp2!=null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())){
            //同名且不同id，不能继续修改
            return ServiceResultEnum.SAME_GOODS_EXIST.getResult();
        }
        if(goodsCategoryMapper.updateById(goodsCategory)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public List<GoodsCategory> getCategoriesForIndex() {
        return ;
    }

    @Override
    public GoodsCategory getCategoriesForSearch(Long categoryId) {
        return null;
    }

    @Override
    public PageResult getCategoriesPage(PageQueryUtil pageQueryUtil) {
        return null;
    }

    @Override
    public GoodsCategory getGoodsCategoryById(Long categoryId) {
        return goodsCategoryMapper.selectById(categoryId);
    }

    @Override
    public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> singletonList, int level) {
        HashMap<String, String> map = new HashMap<>();
        map.put("parent_ids",)
        return goodsCategoryMapper.selectByMap();
    }

    @Override
    public String saveCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp= goodsCategoryMapper.selectById(goodsCategory.getCategoryId());
        if(temp == null){
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("category_level",goodsCategory.getCategoryLevel());
        map.put("category_name",goodsCategory.getCategoryName());
        GoodsCategory temp2= (GoodsCategory) goodsCategoryMapper.selectByMap(map);
        if(temp2!=null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())){
            //同名且不同id 不能继续修改
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if(goodsCategoryMapper.updateById(goodsCategory)>0){
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public boolean deleteBatch(Integer[] ids) {
        if(ids.length<1){
            return false;
        }
        return goodsCategoryMapper.deleteBatchIds(Arrays.asList(ids))>0;
    }
}
