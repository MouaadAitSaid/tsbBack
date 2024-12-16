package com.tsp.controllers;

import com.generic.GenericController;
import com.tsp.dtos.UserInputDTO;
import com.tsp.dtos.UserOutputDTO;
import com.tsp.mappers.UserMapper;
import com.tsp.models.User;
import com.tsp.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController extends GenericController<User, UserInputDTO, UserOutputDTO> {
    public UserController(UserService userService) {
        super(userService);
    }
}

