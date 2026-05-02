package com.mujahid.trackify.domain.dto.request;

/**
 * DTO for creating or updating a User.
 */
public record UserRequestDto(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
