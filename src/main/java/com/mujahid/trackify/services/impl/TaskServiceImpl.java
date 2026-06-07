package com.mujahid.trackify.services.impl;

import com.mujahid.trackify.domain.dto.request.TaskPriorityRequest;
import com.mujahid.trackify.domain.dto.request.TaskRequest;
import com.mujahid.trackify.domain.dto.request.TaskStatusRequest;
import com.mujahid.trackify.domain.dto.response.TaskResponse;
import com.mujahid.trackify.domain.entities.Task;
import com.mujahid.trackify.domain.entities.TaskList;
import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.domain.enums.TaskPriority;
import com.mujahid.trackify.domain.enums.TaskStatus;
import com.mujahid.trackify.exceptions.ResourceNotFoundException;
import com.mujahid.trackify.mappers.TaskMapper;
import com.mujahid.trackify.repositories.TaskListRepository;
import com.mujahid.trackify.repositories.TaskRepository;
import com.mujahid.trackify.security.SecurityUtils;
import com.mujahid.trackify.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final TaskMapper taskMapper;
    private final SecurityUtils securityUtils;

    @Override
    public List<TaskResponse> getAllTasksForTaskList(UUID taskListId) {
        User user = securityUtils.getCurrentUser();
        validateTaskListOwnership(taskListId, user.getId());

        return taskRepository.findAllByTaskListIdAndTaskListUserId(taskListId, user.getId())
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TaskResponse createTask(UUID taskListId, TaskRequest taskRequest) {
        User user = securityUtils.getCurrentUser();
        TaskList taskList = findOwnedTaskList(taskListId, user.getId());
        Task task = taskMapper.toEntity(taskRequest);
        task.setTaskList(taskList);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse getTaskById(UUID taskListId, UUID taskId) {
        User user = securityUtils.getCurrentUser();
        return taskMapper.toResponse(
                findOwnedTask(taskListId, taskId, user.getId())
        );
    }

    @Override
    @Transactional
    public TaskResponse updateTask(UUID taskListId, UUID taskId, TaskRequest taskRequest) {
        User user = securityUtils.getCurrentUser();
        Task task = findOwnedTask(taskListId, taskId, user.getId());
        taskMapper.updateEntityFromRequest(taskRequest, task);
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(UUID taskListId, UUID taskId, TaskStatusRequest request) {
        User user = securityUtils.getCurrentUser();
        Task task = findOwnedTask(taskListId, taskId, user.getId());
        task.setTaskStatus(request.status());
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskPriority(UUID taskListId, UUID taskId, TaskPriorityRequest request) {
        User user = securityUtils.getCurrentUser();
        Task task = findOwnedTask(taskListId, taskId, user.getId());
        task.setTaskPriority(request.priority());
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public void deleteTask(UUID taskListId, UUID taskId) {
        User user = securityUtils.getCurrentUser();
        Task task = findOwnedTask(taskListId, taskId, user.getId());
        taskRepository.delete(task);
    }

    private void validateTaskListOwnership(UUID taskListId, UUID userId) {
        if (!taskListRepository.existsByIdAndUserId(taskListId, userId)) {
            throw new ResourceNotFoundException("Task list not found with id: " + taskListId);
        }
    }

    private TaskList findOwnedTaskList(UUID taskListId, UUID userId) {
        return taskListRepository.findByIdAndUserId(taskListId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task list not found with id: " + taskListId));
    }

    private Task findOwnedTask(UUID taskListId, UUID taskId, UUID userId) {
        return taskRepository.findByIdAndTaskListIdAndTaskListUserId(taskId, taskListId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task not found with id: " + taskId));
    }
}
