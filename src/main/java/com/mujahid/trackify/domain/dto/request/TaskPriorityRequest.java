package com.mujahid.trackify.domain.dto.request;

import com.mujahid.trackify.domain.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "TaskPriorityRequest", description = "Payload used to update a task's priority")
public record TaskPriorityRequest(
        @Schema(description = "New priority value", example = "HIGH")
        @NotNull(message = "Priority must not be null")
        TaskPriority priority
) {}