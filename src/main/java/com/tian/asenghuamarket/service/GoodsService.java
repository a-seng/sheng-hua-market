package com.tian.asenghuamarket.service;

import com.alipay.api.domain.Goods;
import com.tian.asenghuamarket.Dto.GoodsCategory;
import com.tian.asenghuamarket.Dto.GoodsInfo;
import com.tian.asenghuamarket.util.PageQueryUtil;
import com.tian.asenghuamarket.util.PageResult;

import java.util.List;

public interface GoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveNewBeeMallGoods(GoodsInfo goods);

    /**
     * 批量新增商品数据
     *
     * @param goodsInfoList
     * @return
     */
    void batchSaveGoodsInfo(List<GoodsInfo> goodsInfoList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateGoodsInfo(GoodsInfo goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    GoodsInfo getGoodsInfoById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchGoodsInfo(PageQueryUtil pageUtil);
}
