package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.UserUpdateRequest;
import com.mujahid.trackify.domain.dto.response.UserResponse;
import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.exceptions.ResourceNotFoundException;
import com.mujahid.trackify.mappers.UserMapper;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.SecurityUtils;
import com.mujahid.trackify.security.enums.AuthProvider;
import com.mujahid.trackify.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUser_shouldReturnMappedCurrentUserProfile() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("user@example.com").build();
        UserResponse response = new UserResponse(userId, "Amina", "Hassan", "user@example.com", AuthProvider.LOCAL, true, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.getUser();

        assertThat(result).isEqualTo(response);
        verify(userMapper).toResponse(user);
    }

    @Test
    void getUser_shouldThrowWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUser());
    }

    @Test
    void updateUser_shouldMapRequestOntoExistingUser() {
        UUID userId = UUID.randomUUID();
        User currentUser = User.builder().id(userId).build();
        User persistedUser = User.builder().id(userId).firstName("Old").lastName("Name").build();
        UserUpdateRequest request = new UserUpdateRequest("Amina", "Hassan");
        UserResponse response = new UserResponse(userId, "Amina", "Hassan", "user@example.com", AuthProvider.LOCAL, false, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(persistedUser));
        when(userMapper.toResponse(persistedUser)).thenReturn(response);

        UserResponse result = userService.updateUser(request);

        assertThat(result).isEqualTo(response);
        verify(userMapper).updateEntityFromRequest(request, persistedUser);
    }

    @Test
    void deleteUser_shouldDeleteAndThrowWhenNothingWasDeleted() {
        UUID userId = UUID.randomUUID();
        User currentUser = User.builder().id(userId).build();

        when(securityUtils.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.deleteUserById(userId)).thenReturn(0);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser());
    }
}
