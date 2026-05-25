package com.mujahid.trackify.controllers;

import com.mujahid.trackify.security.dto.request.LoginRequest;
import com.mujahid.trackify.security.dto.request.RegisterRequest;
import com.mujahid.trackify.security.dto.response.AuthenticationResponse;
import com.mujahid.trackify.security.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
