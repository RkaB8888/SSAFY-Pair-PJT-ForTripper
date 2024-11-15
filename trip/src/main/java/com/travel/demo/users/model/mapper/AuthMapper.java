package com.travel.demo.users.model.mapper;

import com.travel.demo.users.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    int join(User userInfo);
    User findByEmail(String email);
}
