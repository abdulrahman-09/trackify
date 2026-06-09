package com.mujahid.trackify.controllers;

import com.mujahid.trackify.domain.dto.request.TaskPriorityRequest;
import com.mujahid.trackify.domain.dto.request.TaskRequest;
import com.mujahid.trackify.domain.dto.request.TaskStatusRequest;
import com.mujahid.trackify.domain.dto.response.TaskResponse;
import com.mujahid.trackify.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tasks", description = "Manage tasks inside a task list")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "List tasks", description = "Returns all tasks for the specified task list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks returned successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Task list not found")
    })
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable UUID taskListId){
        return ResponseEntity.ok(taskService.getAllTasksForTaskList(taskListId));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task", description = "Returns one task by its identifier within a task list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task returned successfully"),
            @ApiResponse(responseCode = "404", description = "Task or task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID taskListId,
                                                @PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.getTaskById(taskListId, taskId));
    }

    @PostMapping
    @Operation(summary = "Create task", description = "Creates a new task inside the specified task list.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Task list not found")
    })
    public ResponseEntity<TaskResponse> createTask(@PathVariable UUID taskListId,
                                                   @RequestBody @Valid TaskRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskListId, request));
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update task", description = "Updates an existing task and its details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Task or task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID taskListId,
                                                   @PathVariable UUID taskId,
                                                   @RequestBody @Valid TaskRequest request){
        return ResponseEntity.ok(taskService.updateTask(taskListId, taskId, request));
    }

    @PatchMapping("/{taskId}/priority")
    @Operation(summary = "Update task priority", description = "Updates the priority of an existing task.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Priority updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Task or task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<TaskResponse> updateTaskPriority(@PathVariable UUID taskListId,
                                                           @PathVariable UUID taskId,
                                                           @RequestBody @Valid TaskPriorityRequest request){
        return ResponseEntity.ok(taskService.updateTaskPriority(taskListId, taskId, request));
    }

    @PatchMapping("/{taskId}/status")
    @Operation(summary = "Update task status", description = "Updates the status of an existing task.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Task or task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable UUID taskListId,
                                                         @PathVariable UUID taskId,
                                                         @RequestBody @Valid TaskStatusRequest request){
        return ResponseEntity.ok(taskService.updateTaskStatus(taskListId, taskId, request));
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete task", description = "Deletes a task from the specified task list.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task or task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskListId,
                                           @PathVariable UUID taskId){
        taskService.deleteTask(taskListId,taskId);
        return ResponseEntity.noContent().build();
    }
}
