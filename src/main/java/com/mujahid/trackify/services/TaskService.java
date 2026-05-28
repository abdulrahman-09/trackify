package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.TaskRequestDto;
import com.mujahid.trackify.domain.dto.response.TaskResponseDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponseDto> getAllTasksForTaskList(UUID taskListId);

    TaskResponseDto createTask(UUID taskListId, TaskRequestDto taskRequestDto);

    TaskResponseDto getTaskById(UUID taskListId, UUID taskId);

    TaskResponseDto updateTask(UUID taskListId, UUID taskId, TaskRequestDto taskRequestDto);

    void deleteTask(UUID taskListId, UUID taskId);
}
