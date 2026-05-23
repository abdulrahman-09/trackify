package com.mujahid.trackify.security.dto.response;

public record AuthenticationResponse(
        String accessToken,
        String tokenType
) {
}
