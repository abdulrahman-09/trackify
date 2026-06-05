package com.mujahid.trackify.security;

import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.exceptions.ResourceNotFoundException;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
