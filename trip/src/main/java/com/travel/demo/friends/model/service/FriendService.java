package com.travel.demo.friends.model.service;

import java.util.List;

import com.travel.demo.users.domain.UserDomain;

public interface FriendService {
    boolean addFriend(String fromUserEmail, String toUserNickname);
    boolean removeFriend(String fromUserEmail, String toUserNickname);
    List<UserDomain> getFriends(String userEmail);
    boolean isFriend(String userEmail, String toUserNickname);
}
