package com.mujahid.trackify.security.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "Credentials used to obtain a JWT access token")
public record LoginRequest(
        @Schema(description = "Registered email address", example = "user@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Must be a valid email address")
        String email,

        @Schema(description = "Account password", example = "SuperSecret123")
        @NotBlank(message = "Password is required")
        String password
) {
}
