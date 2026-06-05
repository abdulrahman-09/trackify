package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.TaskRequest;
import com.mujahid.trackify.domain.dto.response.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponse> getAllTasksForTaskList(UUID taskListId);

    TaskResponse createTask(UUID taskListId, TaskRequest taskRequest);

    TaskResponse getTaskById(UUID taskListId, UUID taskId);

    TaskResponse updateTask(UUID taskListId, UUID taskId, TaskRequest taskRequest);

    void deleteTask(UUID taskListId, UUID taskId);
}
