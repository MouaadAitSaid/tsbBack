package com.tsp.mappers;

import com.generic.EntityMapper;
import com.tsp.dtos.TaskInputDTO;
import com.tsp.dtos.TaskOutputDTO;
import com.tsp.enums.TaskStatus;
import com.tsp.models.Task;
import com.tsp.models.User;

public class TaskMapper implements EntityMapper<Task,TaskInputDTO,TaskOutputDTO> {

    // Map Task Entity to TaskOutputDTO
    public  TaskOutputDTO toOutputDTO(Task task) {
        return new TaskOutputDTO(
                task.getId(),
                task.title(),
                task.description(),
                task.color(),
                task.dueDate(),
                task.status(),
                task.user().getId(),
                task.version()
        );
    }

    public Task toEntity(TaskInputDTO taskInputDTO) {
        Task task = new Task();
        task.setTitle(taskInputDTO.title())
                .setDescription(taskInputDTO.description())
                .setColor(taskInputDTO.color())
                .setDueDate(taskInputDTO.dueDate())
                .setStatus(TaskStatus.valueOf(String.valueOf(taskInputDTO.status())))
                .setUserId(taskInputDTO.userId());// Map status (ensure proper case)
        return task;
    }
}