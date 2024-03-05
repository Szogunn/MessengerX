package com.hundredcommits.messengerx.controllers;

import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.payloads.JwtInfoResponse;
import com.hundredcommits.messengerx.payloads.LoginRequest;
import com.hundredcommits.messengerx.payloads.SignupRequest;
import com.hundredcommits.messengerx.repositories.UserRepository;
import com.hundredcommits.messengerx.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.username()).isPresent()){
            return new ResponseEntity<>("Error: Username is already taken", HttpStatus.BAD_REQUEST);
        }

        UserDTO registeredUser = userService.signUp(signupRequest);
        if (registeredUser != null){
            return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("{\"message\":\"Something went wrong\"}", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BadCredentialsException ex){
            return new ResponseEntity<>("Incorrect password or username", HttpStatus.UNAUTHORIZED);
        }

        JwtInfoResponse jwtInfoResponse = userService.logIn(loginRequest);
        if (jwtInfoResponse != null){
            return new ResponseEntity<>(jwtInfoResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>("Something went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
