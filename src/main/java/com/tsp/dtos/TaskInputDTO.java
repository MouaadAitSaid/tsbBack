package com.tsp.dtos;

import com.tsp.enums.TaskStatus;
import com.tsp.models.User;

import java.time.LocalDateTime;

public record TaskInputDTO(
        String title,
        String description,
        String color,
        LocalDateTime dueDate,
        TaskStatus status,
        Long userId // Remove User object
) {}
