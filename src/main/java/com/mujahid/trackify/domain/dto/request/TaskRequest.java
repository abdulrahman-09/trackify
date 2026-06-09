package com.mujahid.trackify.domain.dto.request;

import com.mujahid.trackify.domain.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(name = "TaskRequest", description = "Payload used to create or update a task")
public record TaskRequest(

        @Schema(description = "Task title", example = "Ship release notes")
        @NotBlank(message = "Task title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @Schema(description = "Optional task details", example = "Prepare release notes for the frontend team")
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @Schema(description = "Task due date", example = "2026-06-30T18:00:00")
        @NotNull(message = "Due date is required")
        @Future(message = "Due date must be in the future")
        LocalDateTime dueDate,

        @Schema(description = "Task priority", example = "HIGH")
        @NotNull(message = "Task priority is required")
        TaskPriority taskPriority

) {}
