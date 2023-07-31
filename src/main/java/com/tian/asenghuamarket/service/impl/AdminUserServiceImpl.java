package com.tian.asenghuamarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tian.asenghuamarket.Dto.AdminUser;
import com.tian.asenghuamarket.common.Constants;
import com.tian.asenghuamarket.mapper.AdminUserMapper;
import com.tian.asenghuamarket.service.AdminUserService;
import com.tian.asenghuamarket.util.MD5Util;
import jakarta.annotation.Resource;

public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;
    @Override
    public AdminUser login(String userName, String password) {
        String passwordMD5 = MD5Util.MD5Encode(password, Constants.UTF_ENCODING);
        QueryWrapper<AdminUser> adminUserQueryWrapper = new QueryWrapper<>();
        adminUserQueryWrapper.eq("login_user_name",userName);
        adminUserQueryWrapper.eq("login_password",password);
        return adminUserMapper.selectOne(adminUserQueryWrapper);
    }

    @Override
    public boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser = adminUserMapper.selectById(loginUserId);
        if(adminUser == null){
            return false;
        }
        String newPasswordMd5 = MD5Util.MD5Encode(newPassword, Constants.UTF_ENCODING);
        if(originalPassword.equals(adminUser.getLoginPassword())){
            adminUser.setLoginPassword(newPasswordMd5);
            return adminUserMapper.updateById(adminUser)>0;
        }
        return false;
    }

    @Override
    public boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = adminUserMapper.selectById(loginUserId);
        if(adminUser==null){
            return false;
        }
        //当用户非空
        adminUser.setLoginUserName(loginUserName);
        adminUser.setNickName(nickName);
        return adminUserMapper.updateById(adminUser)>0;
    }


}
