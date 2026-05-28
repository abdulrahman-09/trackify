package com.mujahid.trackify.security;

import com.mujahid.trackify.domain.entities.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Unified principal that works for both JWT (UserDetails) and
 * OAuth2 (OAuth2User) authentication flows.
 */

public class Principal implements UserDetails, OAuth2User {

    private final User user;

    private Map<String, Object> attributes;


    public Principal(User user) {
        this.user = user;
    }

    public Principal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    public UUID getUserId() {
        return user.getId();
    }

    // OAuth2User
    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes != null? attributes : Map.of();
    }

    // UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
