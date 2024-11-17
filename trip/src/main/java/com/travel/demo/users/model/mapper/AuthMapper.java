package com.travel.demo.users.model.mapper;

import com.travel.demo.users.dto.UserLoginRequest;
import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    int join(UserSignUpRequest userInfo);
    UserEntity findByEmail(String email);
}
