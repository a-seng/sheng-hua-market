package com.tian.asenghuamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.asenghuamarket.Dto.AdminUser;
import com.tian.asenghuamarket.Dto.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
