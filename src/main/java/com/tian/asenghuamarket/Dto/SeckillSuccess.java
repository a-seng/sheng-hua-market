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
@TableName(value = "seckill_success")
public class SeckillSuccess {
    @TableId
    private Long secId;
    private Long seckillId;
    private Long userId;
    private Integer state;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
}
