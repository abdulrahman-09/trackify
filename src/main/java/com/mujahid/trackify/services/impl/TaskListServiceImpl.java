package com.mujahid.trackify.services.impl;


import com.mujahid.trackify.domain.dto.request.TaskListRequestDto;
import com.mujahid.trackify.domain.dto.response.TaskListResponseDto;
import com.mujahid.trackify.services.TaskListService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements TaskListService {

    @Override
    public TaskListResponseDto createTaskList(TaskListRequestDto taskListRequestDto) {
        return null;
    }

    @Override
    public List<TaskListResponseDto> getAllTaskListsForCurrentUser() {
        return List.of();
    }

    @Override
    public TaskListResponseDto getTaskListById(UUID taskListId) {
        return null;
    }

    @Override
    public TaskListResponseDto updateTaskList(UUID taskListId, TaskListRequestDto taskListRequestDto) {
        return null;
    }

    @Override
    public void deleteTaskList(UUID taskListId) {

    }
}
