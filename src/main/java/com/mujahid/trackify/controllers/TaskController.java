package com.mujahid.trackify.controllers;

import com.mujahid.trackify.domain.dto.request.TaskPriorityRequest;
import com.mujahid.trackify.domain.dto.request.TaskRequest;
import com.mujahid.trackify.domain.dto.request.TaskStatusRequest;
import com.mujahid.trackify.domain.dto.response.TaskResponse;
import com.mujahid.trackify.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-lists/{taskListId}/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable UUID taskListId){
        return ResponseEntity.ok(taskService.getAllTasksForTaskList(taskListId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID taskListId,
                                                @PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.getTaskById(taskListId, taskId));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@PathVariable UUID taskListId,
                                                   @RequestBody @Valid TaskRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskListId, request));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID taskListId,
                                                   @PathVariable UUID taskId,
                                                   @RequestBody @Valid TaskRequest request){
        return ResponseEntity.ok(taskService.updateTask(taskListId, taskId, request));
    }

    @PatchMapping("/{taskId}/priority")
    public ResponseEntity<TaskResponse> updateTaskPriority(@PathVariable UUID taskListId,
                                                           @PathVariable UUID taskId,
                                                           @RequestBody @Valid TaskPriorityRequest request){
        return ResponseEntity.ok(taskService.updateTaskPriority(taskListId, taskId, request));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable UUID taskListId,
                                                           @PathVariable UUID taskId,
                                                           @RequestBody @Valid TaskStatusRequest request){
        return ResponseEntity.ok(taskService.updateTaskStatus(taskListId, taskId, request));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskListId,
                                                   @PathVariable UUID taskId){
        taskService.deleteTask(taskListId,taskId);
        return ResponseEntity.noContent().build();
    }
}
