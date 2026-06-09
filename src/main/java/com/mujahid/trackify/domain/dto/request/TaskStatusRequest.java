package com.mujahid.trackify.domain.dto.request;

import com.mujahid.trackify.domain.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "TaskStatusRequest", description = "Payload used to update a task's status")
public record TaskStatusRequest(
        @Schema(description = "New status value", example = "IN_PROGRESS")
        @NotNull(message = "Status must not be null")
        TaskStatus status
) {}
