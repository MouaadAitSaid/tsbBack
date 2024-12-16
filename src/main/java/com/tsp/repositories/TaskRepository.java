package com.tsp.repositories;

import com.generic.GenericRepository;
import com.tsp.models.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository  extends GenericRepository<Task> {
}