package com.tian.asenghuamarket.service.impl;

import com.tian.asenghuamarket.Dto.IndexConfig;
import com.tian.asenghuamarket.mapper.GoodsInfoMapper;
import com.tian.asenghuamarket.mapper.IndexConfigMapper;
import com.tian.asenghuamarket.service.IndexConfigService;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;


    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {

        return null;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        return null;
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        return null;
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        return null;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        return null;
    }
}
