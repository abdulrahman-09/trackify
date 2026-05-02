package com.mujahid.trackify.mappers;

import com.mujahid.trackify.domain.dto.request.TaskRequestDto;
import com.mujahid.trackify.domain.dto.response.TaskResponseDto;
import com.mujahid.trackify.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequestDto taskRequestDto);

    @Mapping(target = "taskListId", source = "taskList.id")
    TaskResponseDto toDto(Task task);
}

