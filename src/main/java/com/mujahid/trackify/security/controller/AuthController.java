package com.mujahid.trackify.security.controller;

import com.mujahid.trackify.security.dto.request.LoginRequest;
import com.mujahid.trackify.security.dto.request.RegisterRequest;
import com.mujahid.trackify.security.dto.response.AuthenticationResponse;
import com.mujahid.trackify.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<Map<String, String>> oauthSuccess(
            @CookieValue(name = "jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "No JWT cookie found"));
        }
        return ResponseEntity.ok(Map.of(
                "message", "OAuth2 login successful",
                "token", token
        ));
    }
}
