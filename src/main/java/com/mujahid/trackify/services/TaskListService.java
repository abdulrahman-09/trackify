package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.TaskListRequest;
import com.mujahid.trackify.domain.dto.response.TaskListResponse;

import java.util.List;
import java.util.UUID;

public interface TaskListService {
    TaskListResponse createTaskList(TaskListRequest taskListRequest);

    List<TaskListResponse> getAllTaskListsForCurrentUser();

    TaskListResponse getTaskListById(UUID taskListId);

    TaskListResponse updateTaskList(UUID taskListId, TaskListRequest taskListRequest);

    void deleteTaskList(UUID taskListId);
}
