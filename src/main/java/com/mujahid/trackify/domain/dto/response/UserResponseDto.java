package com.mujahid.trackify.domain.dto.response;

import java.util.UUID;

/**
 * DTO for User responses.
 */
public record UserResponseDto(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
