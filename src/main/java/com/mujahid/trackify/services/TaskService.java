package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.TaskPriorityRequest;
import com.mujahid.trackify.domain.dto.request.TaskRequest;
import com.mujahid.trackify.domain.dto.request.TaskStatusRequest;
import com.mujahid.trackify.domain.dto.response.TaskResponse;
import com.mujahid.trackify.domain.enums.TaskPriority;
import com.mujahid.trackify.domain.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponse> getAllTasksForTaskList(UUID taskListId);

    TaskResponse createTask(UUID taskListId, TaskRequest taskRequest);

    TaskResponse getTaskById(UUID taskListId, UUID taskId);

    TaskResponse updateTask(UUID taskListId, UUID taskId, TaskRequest taskRequest);

    TaskResponse updateTaskStatus(UUID taskListId, UUID taskId, TaskStatusRequest request);

    TaskResponse updateTaskPriority(UUID taskListId, UUID taskId, TaskPriorityRequest request);

    void deleteTask(UUID taskListId, UUID taskId);
}
