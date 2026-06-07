package com.mujahid.trackify.security;

import com.mujahid.trackify.domain.entities.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No authenticated user found");
        }
        Object raw = authentication.getPrincipal();
        if (!(raw instanceof Principal principal)) {
            throw new AccessDeniedException("Unexpected principal type: " + raw.getClass().getName());
        }
        return principal.getUser();
    }


}
