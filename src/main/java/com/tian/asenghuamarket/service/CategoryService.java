package com.tian.asenghuamarket.service;

import com.tian.asenghuamarket.Dto.GoodsCategory;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;

import java.util.List;

public interface CategoryService {
     String updateGoodsCategory(GoodsCategory goodsCategory)

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<GoodsCategory> getCategoriesForIndex();

    /**
     * 返回分类数据(搜索页调用)
     *
     * @param categoryId
     * @return
     */
    GoodsCategory getCategoriesForSearch(Long categoryId);

    PageResult getCategoriesPage(PageQueryUtil pageQueryUtil);

    GoodsCategory getGoodsCategoryById(Long categoryId);

    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> singletonList, int level);

    String saveCategory(GoodsCategory goodsCategory);

    boolean deleteBatch(Integer[] ids);
}
