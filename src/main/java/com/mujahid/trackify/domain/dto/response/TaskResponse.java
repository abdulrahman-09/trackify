package com.mujahid.trackify.domain.dto.response;

import com.mujahid.trackify.domain.enums.TaskPriority;
import com.mujahid.trackify.domain.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority taskPriority,
        TaskStatus taskStatus,
        UUID taskListId,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate
) {
}
