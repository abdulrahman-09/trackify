package com.mujahid.trackify.mappers;

import com.mujahid.trackify.domain.dto.request.UserRequestDto;
import com.mujahid.trackify.domain.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDto userRequestDto);

}
