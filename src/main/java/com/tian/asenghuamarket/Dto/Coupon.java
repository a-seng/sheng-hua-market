package com.tian.asenghuamarket.Dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coupon")
public class Coupon implements Serializable {
    @TableId
    private Long couponId;
    private String couponName;
    private String couponDesc;
    private Integer couponTotal;
    private Integer discoutn;
    private Integer min;
    private Integer couponLimit;
    private Integer couponType;
    private Integer couponStatus;
    private Integer goodsType;
    private String goodsValue;
    private String couponCode;
    @TableField(fill = FieldFill.INSERT,value = "coupon_start_time")
    private Date couponStartTime;
    @TableField(fill = FieldFill.INSERT,value = "coupon_end_time")
    private Date couponEndTime;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private Date updateTime;
    private Integer is_delete;


}
