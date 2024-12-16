package com.tsp.dtos;

import com.tsp.enums.TaskStatus;

import java.time.LocalDateTime;

public record TaskOutputDTO(
        Long id,
        String title,
        String color,
        String description,
        LocalDateTime dueDate,
        TaskStatus status,
        Long userId,
        Long version
) {}
