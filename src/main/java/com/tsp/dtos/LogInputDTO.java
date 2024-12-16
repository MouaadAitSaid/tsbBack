package com.tsp.dtos;

import com.tsp.enums.TaskStatus;

public record LogInputDTO(
        Long taskId,
        String action,
        String oldTitle,
        String newTitle,
        String oldDescription,
        String newDescription,
        TaskStatus oldStatus,
        TaskStatus newStatus,
        Long oldVersion,
        Long newVersion
) {}
