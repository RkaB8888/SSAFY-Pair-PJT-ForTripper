package com.travel.demo.friends.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.travel.demo.friends.model.mapper.FriendMapper;
import com.travel.demo.users.domain.UserDomain;
import com.travel.demo.users.entity.UserEntity;
import com.travel.demo.users.model.mapper.AuthMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {
    private final FriendMapper friendMapper;
    private final AuthMapper authMapper;
	
    @Override
    public boolean addFriend(String fromUserEmail, String toUserNickname) {
        UserEntity toUser = authMapper.findByNickName(toUserNickname);
        if (toUser == null) {
            return false;
        }
        friendMapper.insertFriend(fromUserEmail, toUser.getEmail());
        return true;
    }

    @Override
    public boolean removeFriend(String fromUserEmail, String toUserNickname) {
        UserEntity toUser = authMapper.findByNickName(toUserNickname);
        if (toUser == null) {
            return false;
        }
        friendMapper.deleteFriend(fromUserEmail, toUser.getEmail());
        return true;
    }

    @Override
    public List<UserDomain> getFriends(String userEmail) {
        return friendMapper.selectFriendsByUserEmail(userEmail);
    }

    @Override
    public boolean isFriend(String userEmail, String toUserNickname) {
        UserEntity toUser = authMapper.findByNickName(toUserNickname);
        if (toUser == null) {
            return false;
        }
        int count = friendMapper.countFriendship(userEmail, toUser.getEmail());
        return count > 0;
    }
}
