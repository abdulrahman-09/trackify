package com.mujahid.trackify.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TaskListResponse(
        UUID id,
        String title,
        String description,
        List<TaskResponse> tasks,
        LocalDateTime creationDate,
        LocalDateTime lastUpdateDate
) {
}
