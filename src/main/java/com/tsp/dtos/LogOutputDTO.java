package com.tsp.dtos;

import com.tsp.enums.TaskStatus;

import java.time.LocalDateTime;

public record LogOutputDTO(
        Long id,
        Long taskId,
        String action,
        LocalDateTime timestamp,
        String oldTitle,
        String newTitle,
        String oldDescription,
        String newDescription,
        TaskStatus oldStatus,
        TaskStatus newStatus,
        Long oldVersion,
        Long newVersion
) {}
