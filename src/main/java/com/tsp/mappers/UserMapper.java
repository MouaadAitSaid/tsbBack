package com.tsp.mappers;

import com.generic.EntityMapper;
import com.tsp.dtos.UserInputDTO;
import com.tsp.dtos.UserOutputDTO;
import com.tsp.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper implements EntityMapper<User,UserInputDTO,UserOutputDTO> {

    /**
     * Map a UserInputDTO to a User entity.
     *
     * @param dto UserInputDTO containing input data for user creation or update
     * @return User entity ready to be persisted in the database
     */
    public User toEntity(UserInputDTO dto) {
        // Vérifie que le mot de passe n'est pas vide
        User user = new User();
        if (dto.password() != null && dto.password().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(dto.password()));
        }
        // Hache le mot de passe avant de sauvegarder l'utilisateur
        user.setUsername(dto.username()).setEmail(dto.email()).setCountry(dto.country());
        return user;
    }

    /**
     * Map a User entity to a UserOutputDTO.
     *
     * @param user User entity to be converted to a DTO
     * @return UserOutputDTO containing the user's data for the API response
     */
    public UserOutputDTO toOutputDTO(User user) {
        return new UserOutputDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCountry() != null ? user.getCountry().name() : null // Convert country enum to string
        );
    }
}