package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.payloads.FriendStatus;
import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.jwt.JwtUtils;
import com.hundredcommits.messengerx.payloads.JwtInfoResponse;
import com.hundredcommits.messengerx.payloads.LoginRequest;
import com.hundredcommits.messengerx.payloads.SignupRequest;
import com.hundredcommits.messengerx.repositories.UserRepository;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.utils.ActiveSessionManager;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final ActiveSessionManager activeSessionManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public UserServiceImpl(ActiveSessionManager activeSessionManager, UserRepository userRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.activeSessionManager = activeSessionManager;
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

    @Override
    public Set<String> findUserFriendsName(String username) {
        Optional<User> authenticatedUser = userRepository.findByUsername(username);
        if (authenticatedUser.isEmpty()) {
            return Set.of();
        }

        return authenticatedUser.get().getFriends();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public boolean addFriend(String username, List<String> exceptions) {
        User friend = findUserByUsername(username);
        if (friend == null){
            exceptions.add(String.format("Użytkownik o nazwie %s nie został znaleziony", username));
            return false;
        }

        Optional<User> authenticatedUser = userRepository.findByUsername(SecurityUtils.getAuthenticatedUsername());
        if (authenticatedUser.isEmpty() || friend.getUsername().equals(authenticatedUser.get().getUsername())) {
            exceptions.add("Błąd");
            return false;
        }

        User user = authenticatedUser.get();
        user.getFriends().add(friend.getUsername());
        friend.getFriends().add(user.getUsername());
        try {
            userRepository.saveAll(Set.of(user, friend));
        } catch (Exception ex){
            return false;
        }

        return true;
    }

    @Override
    public boolean removeFriend(String username, List<String> exceptions) {
        Optional<User> authenticatedUser = userRepository.findByUsername(SecurityUtils.getAuthenticatedUsername());
        if (authenticatedUser.isEmpty()){
            exceptions.add("Bad Request");
            return false;
        }

        User friend = findUserByUsername(username);
        if (friend == null){
            return false;
        }

        User user = authenticatedUser.get();
        boolean removedFromUserAuth = user.getFriends().removeIf(friendUsername -> friendUsername.equals(username));
        boolean removedFromUserFriend = friend.getFriends().removeIf(friendUsername -> friendUsername.equals(user.getUsername()));
        if (removedFromUserAuth && removedFromUserFriend){
            userRepository.saveAll(Set.of(user,friend));
            return true;
        }

        return false;
    }

    @Override
    public Set<FriendStatus> findUserFriendsWithStatus(String username) {
        Set<String> userFriendsNames = findUserFriendsName(username);
        Set<String> allActiveUsers = activeSessionManager.getAll();
        return userFriendsNames.stream()
                .map(name -> new FriendStatus(name, allActiveUsers.contains(name)))
                .collect(Collectors.toSet());
    }
}
