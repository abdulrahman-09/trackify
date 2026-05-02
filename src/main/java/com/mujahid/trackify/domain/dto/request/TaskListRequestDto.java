package com.mujahid.trackify.domain.dto.request;

/**
 * DTO for creating or updating a Task List.
 */
public record TaskListRequestDto(
        String title,
        String description

) {
}
