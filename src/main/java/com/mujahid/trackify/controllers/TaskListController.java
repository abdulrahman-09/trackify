package com.mujahid.trackify.controllers;

import com.mujahid.trackify.domain.dto.request.TaskListRequest;
import com.mujahid.trackify.domain.dto.response.TaskListResponse;
import com.mujahid.trackify.services.TaskListService;
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
@RequestMapping("/api/v1/task-lists")
@RequiredArgsConstructor
@Tag(name = "Task Lists", description = "Manage task lists for the current user")
public class TaskListController {
    private final TaskListService taskListService;

    @GetMapping
    @Operation(summary = "List task lists", description = "Returns all task lists owned by the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task lists returned successfully"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<List<TaskListResponse>> getAllTaskLists(){
        return ResponseEntity.ok(taskListService.getAllTaskListsForCurrentUser());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task list", description = "Returns one task list by its identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task list returned successfully"),
            @ApiResponse(responseCode = "404", description = "Task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<TaskListResponse> getTaskList(@PathVariable UUID id){
        return ResponseEntity.ok(taskListService.getTaskListById(id));
    }

    @PostMapping
    @Operation(summary = "Create task list", description = "Creates a new task list for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task list created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<TaskListResponse> createTaskList(@RequestBody @Valid TaskListRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskListService.createTaskList(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task list", description = "Updates an existing task list.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task list updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<TaskListResponse> updateTaskList(@PathVariable UUID id, @RequestBody @Valid TaskListRequest request){
        return ResponseEntity.ok(taskListService.updateTaskList(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task list", description = "Deletes a task list and its associated tasks.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task list deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task list not found"),
            @ApiResponse(responseCode = "401", description = "Authentication required")
    })
    public ResponseEntity<Void> deleteTaskList(@PathVariable UUID id){
        taskListService.deleteTaskList(id);
        return ResponseEntity.noContent().build();
    }
}
