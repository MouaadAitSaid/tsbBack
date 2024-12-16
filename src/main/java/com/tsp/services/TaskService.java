package com.tsp.services;

import com.generic.GenericService;
import com.tsp.dtos.TaskInputDTO;
import com.tsp.dtos.TaskOutputDTO;
import com.tsp.mappers.TaskMapper;
import com.tsp.models.Task;
import com.tsp.models.User;
import com.tsp.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService extends GenericService<Task, TaskInputDTO, TaskOutputDTO> {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        super(taskRepository, new TaskMapper());  // Passer le UserRepository à GenericService
        this.taskRepository = taskRepository;

    }
}
