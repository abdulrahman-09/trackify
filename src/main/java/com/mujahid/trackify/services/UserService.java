package com.mujahid.trackify.services;

public interface UserService {

    void deleteCurrentUser();
    Boolean ExistsByEmail(String email);
}
