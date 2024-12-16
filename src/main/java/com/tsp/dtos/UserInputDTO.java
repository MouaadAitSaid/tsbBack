package com.tsp.dtos;

import com.tsp.enums.Countries;

public record UserInputDTO(
        String username,
        String email,
        String password,
        Countries country
) {}