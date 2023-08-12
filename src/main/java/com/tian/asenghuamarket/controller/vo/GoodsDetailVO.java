package com.tian.asenghuamarket.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 首页配置商品VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailVO implements Serializable {
    private static final long serialVersionUID = 2594416959967125918L;

    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsCoverImg;

    private String[] goodsCarouselList;

    private Integer sellingPrice;

    private Integer originalPrice;

    private String goodsDetailContent;

}
