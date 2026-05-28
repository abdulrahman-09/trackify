package com.mujahid.trackify.domain.dto.response;

import com.mujahid.trackify.security.enums.AuthProvider;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        AuthProvider provider,
        boolean isOAuthUser,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}