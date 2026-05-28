package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.TaskListRequestDto;
import com.mujahid.trackify.domain.dto.response.TaskListResponseDto;

import java.util.List;
import java.util.UUID;

public interface TaskListService {
    TaskListResponseDto createTaskList(TaskListRequestDto taskListRequestDto);

    List<TaskListResponseDto> getAllTaskListsForCurrentUser();

    TaskListResponseDto getTaskListById(UUID taskListId);

    TaskListResponseDto updateTaskList(UUID taskListId, TaskListRequestDto taskListRequestDto);

    void deleteTaskList(UUID taskListId);
}
