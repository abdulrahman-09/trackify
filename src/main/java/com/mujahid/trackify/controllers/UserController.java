package com.mujahid.trackify.controllers;

import com.mujahid.trackify.domain.dto.request.UserUpdateRequest;
import com.mujahid.trackify.domain.dto.response.UserResponse;

import com.mujahid.trackify.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(){
        return ResponseEntity.ok(userService.getUser());
    }

    @PatchMapping
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserUpdateRequest request){
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(){
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
