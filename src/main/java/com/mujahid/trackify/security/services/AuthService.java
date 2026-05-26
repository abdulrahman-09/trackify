package com.mujahid.trackify.security.services;

import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.exceptions.EmailAlreadyInUseException;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.Principal;
import com.mujahid.trackify.security.UserDetailsServiceImpl;
import com.mujahid.trackify.security.dto.request.LoginRequest;
import com.mujahid.trackify.security.dto.request.RegisterRequest;
import com.mujahid.trackify.security.dto.response.AuthenticationResponse;
import com.mujahid.trackify.security.enums.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;


    public AuthenticationResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyInUseException(request.email());
        }
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .provider(AuthProvider.LOCAL)
                .build();

        userRepository.save(user);
        Principal principal = new Principal(user);
        String token = jwtService.generateToken(principal);
        return buildResponse(token);
    }



    public AuthenticationResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        Principal principal = (Principal) userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.generateToken(principal);

        return buildResponse(token);
    }

    private AuthenticationResponse buildResponse(String token) {

        return new AuthenticationResponse(token, "Bearer");
    }
}
