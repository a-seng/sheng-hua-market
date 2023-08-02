package com.tian.asenghuamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.asenghuamarket.Dto.AdminUser;
import com.tian.asenghuamarket.Dto.GoodsInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsInfoMapper extends BaseMapper<GoodsInfo> {
    boolean selectByCategoryIdAndName(String goodsName, Long goodsCategoryId);
}
