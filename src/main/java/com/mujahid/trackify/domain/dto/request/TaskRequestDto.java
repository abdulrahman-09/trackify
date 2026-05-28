package com.mujahid.trackify.domain.dto.request;

import com.mujahid.trackify.domain.enums.TaskPriority;

import java.time.LocalDateTime;

public record TaskRequestDto(
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority taskPriority
) {
}
