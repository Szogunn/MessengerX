package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.jwt.JwtUtils;
import com.hundredcommits.messengerx.payloads.JwtInfoResponse;
import com.hundredcommits.messengerx.payloads.LoginRequest;
import com.hundredcommits.messengerx.payloads.SignupRequest;
import com.hundredcommits.messengerx.repositories.UserRepository;
import com.hundredcommits.messengerx.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserDTO signUp(SignupRequest signupRequest) {
        User newUser = new User(signupRequest.username(), encoder.encode(signupRequest.password()), signupRequest.email());
        userRepository.save(newUser);

        return new UserDTO(newUser.getUsername());
    }

    @Override
    public JwtInfoResponse logIn(LoginRequest loginRequest) {
        String token = jwtUtils.generateToken(loginRequest.username());

        return new JwtInfoResponse(loginRequest.username(), token);
    }
}
