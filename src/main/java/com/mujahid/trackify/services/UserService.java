package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.UserRequestDto;

import java.util.UUID;

public interface UserService {

    void deleteCurrentUser();
    Boolean ExistsByEmail(String email);
}
