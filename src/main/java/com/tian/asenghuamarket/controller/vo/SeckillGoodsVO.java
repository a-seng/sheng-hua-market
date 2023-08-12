package com.tian.asenghuamarket.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 搜索列表页商品VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillGoodsVO implements Serializable {
    private static final long serialVersionUID = -8719192110998138980L;

    private Long seckillId;

    private Integer stockNum;

    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private String goodsDetailContent;

    private String goodsCoverImg;

    private Integer sellingPrice;

    private Integer seckillPrice;

    private Date seckillBegin;

    private Date seckillEnd;

    private String seckillBeginTime;

    private String seckillEndTime;

    private Long startDate;

    private Long endDate;
}
