package com.mujahid.trackify.services.impl;

import com.mujahid.trackify.domain.dto.request.UserUpdateRequest;
import com.mujahid.trackify.domain.dto.response.UserResponse;
import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.exceptions.ResourceNotFoundException;
import com.mujahid.trackify.mappers.UserMapper;
import com.mujahid.trackify.repositories.UserRepository;
import com.mujahid.trackify.security.SecurityUtils;
import com.mujahid.trackify.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;

    @Override
    public UserResponse getUser() {
        User user = securityUtils.getCurrentUser();
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request) {
        User user = securityUtils.getCurrentUser();
        User userToUpdate =  userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getId()));
        userMapper.updateEntityFromRequest(request, userToUpdate);
        return userMapper.toResponse(userToUpdate);
    }


    @Override
    @Transactional
    public void deleteUser() {
        User user = securityUtils.getCurrentUser();
        int deleted = userRepository.deleteUserById(user.getId());
        if (deleted == 0) {
            throw new ResourceNotFoundException("User not found with id: " + user.getId());
        }
    }
}
