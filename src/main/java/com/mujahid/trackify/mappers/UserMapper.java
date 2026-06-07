package com.mujahid.trackify.mappers;

import com.mujahid.trackify.domain.dto.request.UserUpdateRequest;
import com.mujahid.trackify.domain.dto.response.UserResponse;
import com.mujahid.trackify.domain.entities.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "isOAuthUser", expression = "java(user.isOAuthUser())")
    UserResponse toResponse(User user);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "authProviderId", ignore = true)
    @Mapping(target = "taskLists", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
