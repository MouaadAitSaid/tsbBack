package com.tsp.repositories;

import com.generic.GenericRepository;
import com.tsp.models.Log;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository  extends GenericRepository<Log> {
}