package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.payloads.JwtInfoResponse;
import com.hundredcommits.messengerx.payloads.LoginRequest;
import com.hundredcommits.messengerx.payloads.SignupRequest;

import java.util.List;
import java.util.Set;

public interface UserService {

    UserDTO signUp(SignupRequest signupRequest);
    JwtInfoResponse logIn(LoginRequest loginRequest);
    Set<String> findUserFriendsName(String username);
    User findUserByUsername(String username);
    boolean addFriend(String username, List<String> exceptions);
    boolean removeFriend(String username, List<String> exceptions);
    boolean isUsersAreFriends(String firstUser, String secondUser);
}
