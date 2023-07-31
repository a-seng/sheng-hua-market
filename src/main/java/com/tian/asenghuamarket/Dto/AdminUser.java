package com.tian.asenghuamarket.Dto;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="admin_user")
public class AdminUser implements Serializable {
    @TableId
    private Integer adminUserId;
    private String loginUserName;
    private String loginPassword;
    private String nickName;
    private Integer locked;
}
