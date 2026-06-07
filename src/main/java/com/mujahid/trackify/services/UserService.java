package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.UserUpdateRequest;
import com.mujahid.trackify.domain.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UserResponse getUser();
    UserResponse updateUser(UserUpdateRequest request);
    void deleteUser();
}
