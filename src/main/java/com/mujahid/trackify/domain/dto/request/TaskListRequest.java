package com.mujahid.trackify.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "TaskListRequest", description = "Payload used to create or update a task list")
public record TaskListRequest(

        @Schema(description = "Visible title of the task list", example = "Work")
        @NotBlank(message = "Task list title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @Schema(description = "Optional summary of the task list", example = "Tasks for the current sprint")
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description

) {}
