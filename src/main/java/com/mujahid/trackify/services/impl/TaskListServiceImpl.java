package com.mujahid.trackify.services.impl;


import com.mujahid.trackify.domain.dto.request.TaskListRequest;
import com.mujahid.trackify.domain.dto.response.TaskListResponse;
import com.mujahid.trackify.domain.entities.TaskList;
import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.exceptions.ResourceNotFoundException;
import com.mujahid.trackify.mappers.TaskListMapper;
import com.mujahid.trackify.repositories.TaskListRepository;
import com.mujahid.trackify.security.SecurityUtils;
import com.mujahid.trackify.services.TaskListService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskListServiceImpl implements TaskListService {

    private final SecurityUtils securityUtils;
    private final TaskListMapper taskListMapper;
    private final TaskListRepository taskListRepository;

    @Override
    @Transactional
    public TaskListResponse createTaskList(TaskListRequest taskListRequest) {
        User user = securityUtils.getCurrentUser();
        TaskList taskList = taskListMapper.toEntity(taskListRequest);

        taskList.setUser(user);
        TaskList savedTaskList = taskListRepository.save(taskList);

        return taskListMapper.toResponse(savedTaskList);
    }

    @Override
    public List<TaskListResponse> getAllTaskListsForCurrentUser() {
        User user = securityUtils.getCurrentUser();
        List<TaskList> taskLists = taskListRepository.findByUserId(user.getId());
        return taskLists.stream().map(taskListMapper::toResponse).toList();
    }

    @Override
    public TaskListResponse getTaskListById(UUID taskListId) {
        User user = securityUtils.getCurrentUser();
        TaskList taskList = taskListRepository.findByIdAndUserId(taskListId, user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Task list not found with id: " + taskListId)
        );

        return taskListMapper.toResponse(taskList);
    }

    @Override
    @Transactional
    public TaskListResponse updateTaskList(UUID taskListId, TaskListRequest taskListRequest) {
        User user = securityUtils.getCurrentUser();
        TaskList taskList = taskListRepository.findByIdAndUserId(taskListId, user.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Task list not found with id: " + taskListId)
        );

        taskListMapper.updateEntityFromRequest(taskListRequest, taskList);
        TaskList updatedTaskList = taskListRepository.save(taskList);

        return taskListMapper.toResponse(updatedTaskList);
    }

    @Override
    @Transactional
    public void deleteTaskList(UUID taskListId) {
        User user = securityUtils.getCurrentUser();

        int deleted = taskListRepository.deleteByIdAndUserId(taskListId, user.getId());

        if (deleted == 0) {
            throw new ResourceNotFoundException("Task list not found with id: " + taskListId);
        }

    }
}
