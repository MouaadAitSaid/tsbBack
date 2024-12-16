package com.tsp.repositories;

import com.generic.GenericRepository;
import com.tsp.models.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {
}