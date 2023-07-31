package com.tian.asenghuamarket.Dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order")
public class Order {
    @TableId
    private Long orderId;
    private String orderNo;
    private Long userId;
    private Integer totalPrice;
    private Integer payStatus;
    private Integer payType;
    @TableField(fill= FieldFill.INSERT,value = "pay_time")
    private Date payTime;
    private Integer orderStatus;
    private String extraInfo;
    private String userName;
    private String userPhone;
    private String userAddress;
    private Integer isDelete;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
    @TableField(fill=FieldFill.INSERT_UPDATE,value = "update_time")
    private Date updateTime;
}
