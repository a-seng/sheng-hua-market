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
public class UserCouponRecord {
    @TableId
    private Long couponUserId;
    private Long userId;
    private Long couponId;
    private Integer useStatus;
    @TableField(fill = FieldFill.INSERT,value = "used_time")
    private Date usedTime;
    private Long orderId;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
    @TableField(fill=FieldFill.INSERT_UPDATE,value = "update_time")
    private Date updateTime;
    private Integer isDeleted;
}
