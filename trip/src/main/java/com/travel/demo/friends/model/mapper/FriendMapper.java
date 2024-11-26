package com.travel.demo.friends.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.travel.demo.users.domain.UserDomain;

@Mapper
public interface FriendMapper {
    void insertFriend(@Param("fromUserEmail") String fromUserEmail, @Param("toUserEmail") String toUserEmail);
    void deleteFriend(@Param("fromUserEmail") String fromUserEmail, @Param("toUserEmail") String toUserEmail);
    List<UserDomain> selectFriendsByUserEmail(String userEmail);
    int countFriendship(@Param("fromUserEmail") String fromUserEmail, @Param("toUserEmail") String toUserEmail);
}
