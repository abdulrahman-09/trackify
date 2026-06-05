package com.mujahid.trackify.services.impl;


import com.mujahid.trackify.domain.dto.request.TaskListRequest;
import com.mujahid.trackify.domain.dto.response.TaskListResponse;
import com.mujahid.trackify.services.TaskListService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements TaskListService {

    @Override
    public TaskListResponse createTaskList(TaskListRequest taskListRequest) {
        return null;
    }

    @Override
    public List<TaskListResponse> getAllTaskListsForCurrentUser() {
        return List.of();
    }

    @Override
    public TaskListResponse getTaskListById(UUID taskListId) {
        return null;
    }

    @Override
    public TaskListResponse updateTaskList(UUID taskListId, TaskListRequest taskListRequest) {
        return null;
    }

    @Override
    public void deleteTaskList(UUID taskListId) {

    }
}
