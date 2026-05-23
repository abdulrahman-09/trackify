package com.mujahid.trackify.security.services;

import com.mujahid.trackify.security.dto.request.LoginRequest;
import com.mujahid.trackify.security.dto.request.RegisterRequest;
import com.mujahid.trackify.security.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public AuthenticationResponse register(RegisterRequest request){
        // To do
        return null;
    }

    public AuthenticationResponse register(LoginRequest request){
        // To do
        return null;
    }
}
