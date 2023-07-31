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
@TableName(value = "seckill")
public class Seckill {
    @TableId
    private Long seckillId;
    private Long goodsId;
    private Integer seckillPrice;
    private Integer seckillNum;
    private Integer seckillStatus;
    @TableField(fill = FieldFill.INSERT,value = "seckill_begin")
    private Date seckillBegin;
    @TableField(fill=FieldFill.INSERT,value = "seckill_end")
    private Date seckillEnd;
    private Integer seckillRank;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
    @TableField(fill=FieldFill.INSERT_UPDATE,value = "update_time")
    private Date updateTime;
    private Integer isDeleted;
}
