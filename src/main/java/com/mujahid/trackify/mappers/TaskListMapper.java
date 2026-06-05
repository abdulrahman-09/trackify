package com.mujahid.trackify.mappers;

import com.mujahid.trackify.domain.dto.request.TaskListRequest;
import com.mujahid.trackify.domain.dto.response.TaskListResponse;
import com.mujahid.trackify.domain.entities.TaskList;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {
    TaskList toEntity(TaskListRequest taskListRequest);

    TaskListResponse toDto(TaskList taskList);
}


