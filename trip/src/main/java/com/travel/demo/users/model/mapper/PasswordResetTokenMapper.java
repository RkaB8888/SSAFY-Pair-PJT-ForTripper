package com.travel.demo.users.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.travel.demo.users.entity.PasswordResetToken;

@Mapper
public interface PasswordResetTokenMapper {
    void saveToken(PasswordResetToken token);
    PasswordResetToken findByToken(String token);
    void deleteToken(String token);
}