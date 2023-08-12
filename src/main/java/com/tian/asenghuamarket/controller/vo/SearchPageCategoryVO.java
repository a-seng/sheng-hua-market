package com.tian.asenghuamarket.controller.vo;

import com.tian.asenghuamarket.Dto.GoodsCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索页面分类数据VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPageCategoryVO implements Serializable {


    private String firstLevelCategoryName;

    private List<GoodsCategory> secondLevelCategoryList;

    private String secondLevelCategoryName;

    private List<GoodsCategory> thirdLevelCategoryList;

    private String currentCategoryName;

}
