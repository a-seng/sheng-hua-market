package com.tian.asenghuamarket.service.impl;

import com.tian.asenghuamarket.Dto.GoodsCategory;
import com.tian.asenghuamarket.service.GoodsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsServiceImpl implements GoodsService {
    @Override
    public List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> singletonList, int level) {
        Map<String,Object>map=new HashMap<>();
        map.put("parent_ids",)
        return null;
    }
}
