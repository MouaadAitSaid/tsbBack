package com.tsp.dtos;

public record UserOutputDTO(
        Long id,
        String username,
        String email,
        String country
) {}
