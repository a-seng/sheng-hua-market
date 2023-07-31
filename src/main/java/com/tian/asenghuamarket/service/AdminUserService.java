package com.tian.asenghuamarket.service;

import com.tian.asenghuamarket.Dto.AdminUser;

public interface AdminUserService {
    AdminUser login(String userName, String password);

    boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

    boolean updateName(Integer loginUserId, String loginUserName, String nickName);
}
