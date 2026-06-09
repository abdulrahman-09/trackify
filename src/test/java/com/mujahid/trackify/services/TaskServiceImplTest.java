package com.mujahid.trackify.services;

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
import com.mujahid.trackify.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void getAllTasksForTaskList_shouldReturnOnlyOwnedTasks() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Task task = Task.builder().id(UUID.randomUUID()).title("Ship notes").build();
        TaskResponse response = new TaskResponse(task.getId(), "Ship notes", null, null, null, null, taskListId, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.existsByIdAndUserId(taskListId, userId)).thenReturn(true);
        when(taskRepository.findAllByTaskListIdAndTaskListUserId(taskListId, userId)).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        List<TaskResponse> result = taskService.getAllTasksForTaskList(taskListId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Ship notes");
        verify(taskRepository).findAllByTaskListIdAndTaskListUserId(taskListId, userId);
    }

    @Test
    void createTask_shouldAttachTaskListAndPersistTask() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        TaskList taskList = TaskList.builder().id(taskListId).title("Backlog").build();
        TaskRequest request = new TaskRequest("Ship notes", "Prepare release notes", LocalDateTime.now().plusDays(1), TaskPriority.HIGH);
        Task task = Task.builder().title("Ship notes").build();
        TaskResponse response = new TaskResponse(UUID.randomUUID(), "Ship notes", "Prepare release notes", request.dueDate(), TaskPriority.HIGH, TaskStatus.OPEN, taskListId, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.findByIdAndUserId(taskListId, userId)).thenReturn(java.util.Optional.of(taskList));
        when(taskMapper.toEntity(request)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(response);

        TaskResponse result = taskService.createTask(taskListId, request);

        assertThat(result.title()).isEqualTo("Ship notes");
        assertThat(task.getTaskList()).isSameAs(taskList);
        verify(taskRepository).save(task);
    }

    @Test
    void updateTaskStatus_shouldChangeStatusAndReturnMappedResponse() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Task task = Task.builder().id(taskId).taskStatus(TaskStatus.OPEN).build();

        TaskResponse response = new TaskResponse(taskId, "Ship notes", null, null, TaskPriority.HIGH, TaskStatus.IN_PROGRESS, taskListId, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndTaskListIdAndTaskListUserId(taskId, taskListId, userId)).thenReturn(java.util.Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        TaskResponse result = taskService.updateTaskStatus(taskListId, taskId, new TaskStatusRequest(TaskStatus.IN_PROGRESS));

        assertThat(task.getTaskStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(result).isNotNull();
        verify(taskMapper).toResponse(task);
    }

    @Test
    void updateTaskPriority_shouldChangePriorityAndReturnMappedResponse() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Task task = Task.builder().id(taskId).taskPriority(TaskPriority.LOW).build();

        TaskResponse response = new TaskResponse(taskId, "Ship notes", null, null, TaskPriority.HIGH, TaskStatus.OPEN, taskListId, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndTaskListIdAndTaskListUserId(taskId, taskListId, userId)).thenReturn(java.util.Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(response);

        TaskResponse result = taskService.updateTaskPriority(taskListId, taskId, new TaskPriorityRequest(TaskPriority.HIGH));

        assertThat(task.getTaskPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(result).isNotNull();
        verify(taskMapper).toResponse(task);
    }

    @Test
    void deleteTask_shouldDeleteOwnedTask() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Task task = Task.builder().id(taskId).build();

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndTaskListIdAndTaskListUserId(taskId, taskListId, userId)).thenReturn(java.util.Optional.of(task));

        taskService.deleteTask(taskListId, taskId);

        verify(taskRepository).delete(task);
    }

    @Test
    void getAllTasksForTaskList_shouldThrowWhenTaskListNotOwned() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.existsByIdAndUserId(taskListId, userId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.getAllTasksForTaskList(taskListId));
    }

    @Test
    void createTask_shouldThrowWhenTaskListNotFound() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        TaskRequest request = new TaskRequest("Title", "Desc", LocalDateTime.now().plusDays(1), TaskPriority.HIGH);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.findByIdAndUserId(taskListId, userId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(taskListId, request));
    }

    @Test
    void getTaskById_shouldThrowWhenTaskNotFound() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndTaskListIdAndTaskListUserId(taskId, taskListId, userId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(taskListId, taskId));
    }

    @Test
    void updateTask_shouldThrowWhenTaskNotFound() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        TaskRequest request = new TaskRequest("Updated", "Desc", LocalDateTime.now().plusDays(1), TaskPriority.LOW);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskRepository.findByIdAndTaskListIdAndTaskListUserId(taskId, taskListId, userId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(taskListId, taskId, request));
    }
}
