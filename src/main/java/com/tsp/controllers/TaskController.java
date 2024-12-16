package com.tsp.controllers;

import com.generic.GenericController;
import com.tsp.dtos.TaskInputDTO;
import com.tsp.dtos.TaskOutputDTO;
import com.tsp.mappers.TaskMapper;
import com.tsp.services.TaskService;
import com.tsp.services.UserService;
import com.tsp.models.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController  extends GenericController<Task, TaskInputDTO, TaskOutputDTO> {
    public TaskController(TaskService taskService) {
        super(taskService);
    }
}