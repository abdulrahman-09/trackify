package com.mujahid.trackify.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskListResponseDto(
        UUID id,
        String title,
        String description,
        List<TaskResponseDto> tasks,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate
) {
}
