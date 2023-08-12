package com.tian.asenghuamarket.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 首页配置商品VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemVO implements Serializable {


    private Long goodsId;

    private Integer goodsCount;

    private String goodsName;

    private String goodsCoverImg;

    private Integer sellingPrice;

}
