package com.travel.demo.users.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.travel.demo.users.dto.UserSignUpRequest;
import com.travel.demo.users.entity.UserEntity;

@Mapper
public interface AuthMapper {
    int join(UserSignUpRequest userInfo);
    UserEntity findByEmail(String email);
    UserEntity findByNickName(String nickname);
    int updateUserVerificationStatus(UserEntity userEntity);
    void updatePassword(UserEntity user);
    void updateProfileImage(@Param("email") String email, @Param("profileImage") String profileImage);
    void updateNickname(@Param("email") String email, @Param("nickname") String nickname);
    void deleteUser(@Param("email") String email);
    List<UserEntity> findByNicknameContaining(String nickname);
}
