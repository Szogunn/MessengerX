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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
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

    @GetMapping(path = "/signup")
    public String signupView() {
        return "user/signup";
    }

    @PostMapping(path = "/signup")
    public String signUp(@ModelAttribute SignupRequest signupRequest, Model model) {
        String signupError = null;
        if (signupRequest == null){
            signupError = "Credentials cannot be empty";
        }

        if (userRepository.findByUsername(signupRequest.username()).isPresent()){
            signupError = "Username is already taken";
        }

        if (userRepository.findByEmail(signupRequest.email()).isPresent()){
            signupError = "Email is already taken";
        }

        if (signupError == null){
            UserDTO registeredUser = userService.signUp(signupRequest);
            if (registeredUser != null) {
                model.addAttribute("signupSuccess", true);
                return "user/signup";
            }
        }

        model.addAttribute("signupError", signupError);
        return "user/signup";
    }

    @GetMapping(path = "/login")
    public String loginView() {
        return "user/login";
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
            System.out.println("logged in");
            return new ResponseEntity<>(jwtInfoResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>("Something went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
