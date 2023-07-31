package com.tian.asenghuamarket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tian.asenghuamarket.Dto.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
