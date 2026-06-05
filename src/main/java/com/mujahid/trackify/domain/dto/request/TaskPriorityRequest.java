package com.mujahid.trackify.domain.dto.request;

import com.mujahid.trackify.domain.enums.TaskPriority;
import jakarta.validation.constraints.NotNull;

public record TaskPriorityRequest(
        @NotNull(message = "Priority must not be null")
        TaskPriority priority
) {}