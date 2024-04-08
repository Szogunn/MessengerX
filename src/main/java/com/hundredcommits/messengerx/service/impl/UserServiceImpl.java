package com.hundredcommits.messengerx.service.impl;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.dtos.InvitationDTO;
import com.hundredcommits.messengerx.dtos.UserDTO;
import com.hundredcommits.messengerx.jwt.JwtUtils;
import com.hundredcommits.messengerx.notification.EventNotify;
import com.hundredcommits.messengerx.notification.FriendRequestEvent;
import com.hundredcommits.messengerx.notification.NotificationExecutor;
import com.hundredcommits.messengerx.payloads.JwtInfoResponse;
import com.hundredcommits.messengerx.payloads.LoginRequest;
import com.hundredcommits.messengerx.payloads.SignupRequest;
import com.hundredcommits.messengerx.repositories.UserRepository;
import com.hundredcommits.messengerx.service.InvitationService;
import com.hundredcommits.messengerx.service.UserService;
import com.hundredcommits.messengerx.utils.AppUtil;
import com.hundredcommits.messengerx.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService, EventNotify<FriendRequestEvent> {
    private final UserRepository userRepository;
    private final InvitationService invitationService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    private final NotificationExecutor notificationExecutor;

    public UserServiceImpl(UserRepository userRepository, InvitationService invitationService, PasswordEncoder encoder, JwtUtils jwtUtils, NotificationExecutor notificationExecutor) {
        this.userRepository = userRepository;
        this.invitationService = invitationService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.notificationExecutor = notificationExecutor;
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
        if (friend == null) {
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
    public void inviteFriend(String username) {
        String authUser = SecurityUtils.getAuthenticatedUsername();
        if (AppUtil.isEmpty(username) || username.equals(authUser)) {
            log.warn("Provided name of user is empty or equals authenticated user name");
            return;
        }

        InvitationDTO invitation = new InvitationDTO(authUser, username);
        boolean saved = invitationService.saveInvitation(invitation);
        if (saved) {
            FriendRequestEvent event = new FriendRequestEvent(authUser, username);
            notify(authUser, Set.of(username), event);
        }
    }

    @Override
    public boolean responseForInvitation(boolean isAccepted, String username) {
        String authUserName = SecurityUtils.getAuthenticatedUsername();
        InvitationDTO invitationDTO = new InvitationDTO(username, authUserName);
        boolean isValid = invitationService.responseForInvitation(isAccepted, invitationDTO);

        if (isValid) {
//            userRepository.findByUsername(authUserName).ifPresent(user -> user.getFriends().add(username));
//            userRepository.findByUsername(username).ifPresent(user -> user.getFriends().add(authUserName));
//            todo: zaimplementować metodę którą przyjmuję Consumera jako funkcję i będzie zapisywała wynik działania jakim jest User do bazy danych
            User friend = findUserByUsername(username);
            User authenticatedUser = findUserByUsername(authUserName);
            if (friend != null && authenticatedUser != null){
                friend.getFriends().add(authUserName);
                authenticatedUser.getFriends().add(username);
                userRepository.saveAll(Set.of(friend, authenticatedUser));
                return true;
            }

            return false;
        }

        return false;
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
    public void notify(String senderNotify, Set<String> recipientsNames, FriendRequestEvent event) {
        List<String> errors = notificationExecutor.notify(senderNotify, recipientsNames, event);

        if (!errors.isEmpty()){
            for (String string : errors) {
                //todo: obsłużyć wyjątki powstałe podczas przesyłania eventów
            }
        }
    }
}
