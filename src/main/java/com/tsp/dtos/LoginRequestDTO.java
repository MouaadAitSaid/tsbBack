package com.tsp.dtos;

/**
 * DTO pour encapsuler les données de la requête de connexion.
 */
public class LoginRequestDTO {
    private String username;
    private String password;

    // Getters et Setters

    public String username() {
        return username;
    }

    public LoginRequestDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String password() {
        return password;
    }

    public LoginRequestDTO setPassword(String password) {
        this.password = password;
        return this;
    }
}
