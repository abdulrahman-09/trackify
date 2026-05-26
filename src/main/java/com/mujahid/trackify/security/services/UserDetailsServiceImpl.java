package com.mujahid.trackify.security.services;

import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("No user found with email: " + email)
        );

        return new Principal(user);
    }
}
