package com.mujahid.trackify.domain.dto.request;


public record UserRequestDto(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
