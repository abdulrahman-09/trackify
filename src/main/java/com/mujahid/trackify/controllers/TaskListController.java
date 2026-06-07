package com.mujahid.trackify.controllers;

import com.mujahid.trackify.domain.dto.request.TaskListRequest;
import com.mujahid.trackify.domain.dto.response.TaskListResponse;
import com.mujahid.trackify.services.TaskListService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-list")
@RequiredArgsConstructor
public class TaskListController {
    private final TaskListService taskListService;

    @GetMapping
    public ResponseEntity<List<TaskListResponse>> getAllTaskLists(){
        return ResponseEntity.ok(taskListService.getAllTaskListsForCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskListResponse> getTaskList(@PathVariable UUID id){
        return ResponseEntity.ok(taskListService.getTaskListById(id));
    }

    @PostMapping
    public ResponseEntity<TaskListResponse> createTaskList(@RequestBody @Valid TaskListRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskListService.createTaskList(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskListResponse> updateTaskList(@PathVariable UUID id, @RequestBody @Valid TaskListRequest request){
        return ResponseEntity.ok(taskListService.updateTaskList(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable UUID id){
        taskListService.deleteTaskList(id);
        return ResponseEntity.noContent().build();
    }
}
