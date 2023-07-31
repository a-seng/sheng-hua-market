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
@TableName(value = "goods_category")
public class GoodsCategory implements Serializable {
    @TableId
    private Integer categoryId;
    private Integer categoryLevel;
    private Integer parentId;
    private String categoryName;
    private Integer couponRank;
    private Integer isDelete;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
    private Integer createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private Date updateTime;
    private Integer updateUser;
}
