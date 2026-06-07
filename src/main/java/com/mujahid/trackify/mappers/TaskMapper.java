package com.mujahid.trackify.mappers;

import com.mujahid.trackify.domain.dto.request.TaskRequest;
import com.mujahid.trackify.domain.dto.response.TaskResponse;
import com.mujahid.trackify.domain.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskList", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    Task toEntity(TaskRequest request);


    @Mapping(target = "taskListId", source = "taskList.id")
    TaskResponse toResponse(Task task);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskList", ignore = true)
    @Mapping(target = "taskStatus", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    void updateEntityFromRequest(TaskRequest request, @MappingTarget Task task);
}



