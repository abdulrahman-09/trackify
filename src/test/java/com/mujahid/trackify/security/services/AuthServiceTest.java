package com.mujahid.trackify.security.services;

import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.exceptions.EmailAlreadyInUseException;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.Principal;
import com.mujahid.trackify.security.dto.request.LoginRequest;
import com.mujahid.trackify.security.dto.request.RegisterRequest;
import com.mujahid.trackify.security.dto.response.AuthenticationResponse;
import com.mujahid.trackify.security.enums.AuthProvider;
import com.mujahid.trackify.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldThrowWhenEmailIsAlreadyTaken() {
        RegisterRequest request = new RegisterRequest("Amina", "Hassan", "user@example.com", "Secret1234");

        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyInUseException.class, () -> authService.register(request));
    }

    @Test
    void register_shouldEncodePasswordAndReturnToken() {
        RegisterRequest request = new RegisterRequest("Amina", "Hassan", "user@example.com", "Secret1234");
        User savedUser = User.builder().id(UUID.randomUUID()).email("user@example.com").provider(AuthProvider.LOCAL).build();

        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("Secret1234")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(Principal.class))).thenReturn("jwt-token");

        AuthenticationResponse response = authService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(captor.getValue().getEmail()).isEqualTo("user@example.com");
        assertThat(captor.getValue().getPassword()).isEqualTo("encoded-password");
        assertThat(captor.getValue().getProvider()).isEqualTo(AuthProvider.LOCAL);
    }

    @Test
    void login_shouldAuthenticateAndReturnToken() {
        LoginRequest request = new LoginRequest("user@example.com", "Secret1234");
        Principal principal = new Principal(User.builder().id(UUID.randomUUID()).email("user@example.com").build());

        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(principal);
        when(jwtService.generateToken(principal)).thenReturn("jwt-token");

        AuthenticationResponse response = authService.login(request);

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        verify(authenticationManager).authenticate(any());
    }
}
