package com.tsp.models;

import com.generic.GenericEntity;
import com.tsp.enums.Countries;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.util.List;

@Entity
@Table(name = "app_user")
public class User extends GenericEntity {

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Countries country;


// Getters et Setters

    public Countries getCountry() {
        return country;
    }

    public User setCountry(Countries country) {
        this.country = country;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public User setTasks(List<Task> tasks) {
        this.tasks = tasks;
        return this;
    }
}
