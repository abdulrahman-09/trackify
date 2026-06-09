package com.mujahid.trackify.security.services;

import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.Principal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnPrincipalWhenUserExists() {
        String email = "user@example.com";
        User user = User.builder().id(UUID.randomUUID()).email(email).build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(email);

        assertThat(result).isInstanceOf(Principal.class);
        assertThat(result.getUsername()).isEqualTo(email);
    }

    @Test
    void loadUserByUsername_shouldThrowWhenUserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }
}
