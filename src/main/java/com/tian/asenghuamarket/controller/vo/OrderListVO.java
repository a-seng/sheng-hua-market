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
public class OrderListVO implements Serializable {

    private Long orderId;

    private String orderNo;

    private Integer totalPrice;

    private Byte payType;

    private Byte orderStatus;

    private String orderStatusString;

    private String userAddress;

    private Date createTime;

    private List<OrderItemVO> newBeeMallOrderItemVOS;

}
