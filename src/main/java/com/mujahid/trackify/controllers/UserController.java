package com.mujahid.trackify.controllers;

import com.mujahid.trackify.domain.dto.response.UserResponse;
import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.security.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping
    public ResponseEntity<UserResponse> getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((Principal) authentication.getPrincipal()).getUser();

        return ResponseEntity.ok(new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getProvider(),
                user.isOAuthUser(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        ));
    }

}
