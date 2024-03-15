package com.hundredcommits.messengerx.utils;

import com.hundredcommits.messengerx.domains.User;
import com.hundredcommits.messengerx.jwt.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    public static String getAuthenticatedUsername() {
        UserDetailsImpl authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser != null){
            return authenticatedUser.getUsername();
        }

        return null;
    }

    public static UserDetailsImpl getAuthenticatedUser(){
        if (!isAuthenticated()){
            return null;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails;
        }

        return null;
    }
}
