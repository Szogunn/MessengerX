package com.hundredcommits.messengerx.service;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.payloads.JwtInfoResponse;
import com.hundredcommits.messengerx.payloads.LoginRequest;
import com.hundredcommits.messengerx.payloads.SignupRequest;

public interface UserService {

    UserDTO signUp(SignupRequest signupRequest);
    JwtInfoResponse logIn(LoginRequest loginRequest);
    User getAuthenticatedUser();
}
