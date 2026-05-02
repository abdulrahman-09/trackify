package com.mujahid.trackify.domain.dto.request;

import com.mujahid.trackify.domain.enums.TaskPriority;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for creating or updating a Task.
 */
public record TaskRequestDto(
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority taskPriority
) {
}
