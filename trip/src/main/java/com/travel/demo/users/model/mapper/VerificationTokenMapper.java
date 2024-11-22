package com.travel.demo.users.model.mapper;

import com.travel.demo.users.entity.VerificationToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VerificationTokenMapper {
	
    void saveToken(VerificationToken token);
    VerificationToken findByToken(String token);
    void deleteToken(String token);
}