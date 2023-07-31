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
@TableName(value="goods_info")
public class GoodsInfo implements Serializable {
    @TableId
    private Long goodsId;
    private String goodsName;
    private String goodsIntro;
    private Long goodsCategoryId;
    private String goodsCoverImg;
    private String goodsCarousel;
    private String goodsDetailContent;
    private Integer originalPrice;
    private Integer sellingPrice;
    private Integer stockNum;
    private String tag;
    private Integer goodsSellStatus;
    private Integer createUser;
    //字段添加填充内容
    @TableField(fill = FieldFill.INSERT ,value = "create_time")
    private Date createTime;
    private Integer updateUser;
    @TableField(fill = FieldFill.INSERT_UPDATE ,value = "update_time")
    private Date updateTime;

}
