package com.mujahid.trackify.mappers;

import com.mujahid.trackify.domain.dto.request.TaskListRequestDto;
import com.mujahid.trackify.domain.dto.response.TaskListResponseDto;
import com.mujahid.trackify.domain.entities.TaskList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {
    TaskList toEntity(TaskListRequestDto taskListRequestDto);

    TaskListResponseDto toDto(TaskList taskList);
}


