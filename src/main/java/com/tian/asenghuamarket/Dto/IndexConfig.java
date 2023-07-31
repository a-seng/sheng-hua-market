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
@TableName(value = "index_config")
public class IndexConfig {
    @TableId
    private Integer configId;
    private String configName;
    private Integer configType;
    private Long goodsId;
    private String redirectUrl;
    private Integer configRank;
    private Integer isDelete;
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    private Date createTime;
    private Integer createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    private Date updateTime;
    private Integer updateUser;
}
