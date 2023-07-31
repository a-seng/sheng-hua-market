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
@TableName(value = "Order_item")
public class OrderItem {
    @TableId
    private Long orderItemId;
    private Long orderId;
    private Long seckillId;
    private Long goodsId;
    private String goodsName;
    private String goodsCoverImg;
    private Integer sellingPrice;
    private Integer goodsCount;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
}
