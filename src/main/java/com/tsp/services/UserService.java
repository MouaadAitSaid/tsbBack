package com.tsp.services;

import com.tsp.dtos.UserInputDTO;
import com.tsp.dtos.UserOutputDTO;
import com.tsp.mappers.UserMapper;
import com.tsp.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.generic.GenericService;
import com.tsp.models.User;

@Service
public class UserService extends GenericService<User, UserInputDTO, UserOutputDTO> {

    private final UserRepository userRepository;

    // Injection du UserRepository via le constructeur
    public UserService(UserRepository userRepository) {
        super(userRepository, new UserMapper());  // Passer le UserRepository à GenericService
        this.userRepository = userRepository;
    }
}
