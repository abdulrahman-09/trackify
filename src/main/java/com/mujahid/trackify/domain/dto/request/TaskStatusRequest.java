package com.mujahid.trackify.domain.dto.request;

import com.mujahid.trackify.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusRequest(
        @NotNull(message = "Status must not be null")
        TaskStatus status
) {}
