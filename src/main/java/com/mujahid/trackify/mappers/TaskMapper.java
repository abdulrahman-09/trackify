package com.mujahid.trackify.mappers;

import com.mujahid.trackify.domain.dto.request.TaskRequest;
import com.mujahid.trackify.domain.dto.response.TaskResponse;
import com.mujahid.trackify.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequest taskRequest);

    @Mapping(source = "taskList.id", target = "taskListId")
    TaskResponse toDto(Task task);
}


