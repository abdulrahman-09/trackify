package com.mujahid.trackify.services;

import com.mujahid.trackify.domain.dto.request.TaskListRequest;
import com.mujahid.trackify.domain.dto.response.TaskListResponse;
import com.mujahid.trackify.domain.entities.TaskList;
import com.mujahid.trackify.domain.entities.User;
import com.mujahid.trackify.exceptions.ResourceNotFoundException;
import com.mujahid.trackify.mappers.TaskListMapper;
import com.mujahid.trackify.repositories.TaskListRepository;
import com.mujahid.trackify.security.SecurityUtils;
import com.mujahid.trackify.services.impl.TaskListServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskListServiceImplTest {

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private TaskListMapper taskListMapper;

    @Mock
    private TaskListRepository taskListRepository;

    @InjectMocks
    private TaskListServiceImpl taskListService;

    @Test
    void createTaskList_shouldSaveAndReturnResponse() {
        User user = User.builder().id(UUID.randomUUID()).email("user@example.com").build();
        TaskListRequest request = new TaskListRequest("Work", "Tasks for today");
        TaskList entity = TaskList.builder().title("Work").description("Tasks for today").build();
        TaskListResponse response = new TaskListResponse(UUID.randomUUID(), "Work", "Tasks for today", null, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListMapper.toEntity(request)).thenReturn(entity);
        when(taskListRepository.save(entity)).thenReturn(entity);
        when(taskListMapper.toResponse(entity)).thenReturn(response);

        TaskListResponse result = taskListService.createTaskList(request);

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("Work");
        assertThat(entity.getUser()).isSameAs(user);
        verify(taskListRepository).save(entity);
    }

    @Test
    void getAllTaskListsForCurrentUser_shouldReturnOnlyUserOwnedLists() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        TaskList taskList1 = TaskList.builder().id(UUID.randomUUID()).title("Work").build();
        TaskList taskList2 = TaskList.builder().id(UUID.randomUUID()).title("Personal").build();
        TaskListResponse response1 = new TaskListResponse(taskList1.getId(), "Work", null, null, null, null);
        TaskListResponse response2 = new TaskListResponse(taskList2.getId(), "Personal", null, null, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.findByUserId(userId)).thenReturn(List.of(taskList1, taskList2));
        when(taskListMapper.toResponse(taskList1)).thenReturn(response1);
        when(taskListMapper.toResponse(taskList2)).thenReturn(response2);

        List<TaskListResponse> result = taskListService.getAllTaskListsForCurrentUser();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TaskListResponse::title).contains("Work", "Personal");
    }

    @Test
    void getTaskListById_shouldReturnOwnedTaskListOrThrow() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        TaskList taskList = TaskList.builder().id(taskListId).title("Work").build();
        TaskListResponse response = new TaskListResponse(taskListId, "Work", null, null, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.findByIdAndUserId(taskListId, userId)).thenReturn(Optional.of(taskList));
        when(taskListMapper.toResponse(taskList)).thenReturn(response);

        TaskListResponse result = taskListService.getTaskListById(taskListId);

        assertThat(result.title()).isEqualTo("Work");
    }

    @Test
    void getTaskListById_shouldThrowWhenTaskListNotFound() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.findByIdAndUserId(taskListId, userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskListService.getTaskListById(taskListId));
    }

    @Test
    void updateTaskList_shouldModifyAndReturnUpdatedResponse() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        TaskList taskList = TaskList.builder().id(taskListId).title("Old Title").build();
        TaskListRequest request = new TaskListRequest("Updated Title", "Updated description");
        TaskListResponse response = new TaskListResponse(taskListId, "Updated Title", "Updated description", null, null, null);

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.findByIdAndUserId(taskListId, userId)).thenReturn(Optional.of(taskList));
        when(taskListRepository.save(taskList)).thenReturn(taskList);
        when(taskListMapper.toResponse(taskList)).thenReturn(response);

        TaskListResponse result = taskListService.updateTaskList(taskListId, request);

        assertThat(result.title()).isEqualTo("Updated Title");
        verify(taskListMapper).updateEntityFromRequest(request, taskList);
        verify(taskListRepository).save(taskList);
    }

    @Test
    void deleteTaskList_shouldRemoveOwnedTaskList() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.deleteByIdAndUserId(taskListId, userId)).thenReturn(1);

        taskListService.deleteTaskList(taskListId);

        verify(taskListRepository).deleteByIdAndUserId(taskListId, userId);
    }

    @Test
    void deleteTaskList_shouldThrowWhenNotFound() {
        UUID userId = UUID.randomUUID();
        UUID taskListId = UUID.randomUUID();
        User user = User.builder().id(userId).build();

        when(securityUtils.getCurrentUser()).thenReturn(user);
        when(taskListRepository.deleteByIdAndUserId(taskListId, userId)).thenReturn(0);

        assertThrows(ResourceNotFoundException.class, () -> taskListService.deleteTaskList(taskListId));
    }
}
