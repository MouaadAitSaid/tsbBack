package com.tsp.models;

import com.generic.GenericEntity;
import com.tsp.enums.TaskStatus;
import com.utils.CleEtrangere;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Task extends GenericEntity {

    @NotBlank
    @Size(min = 5, max = 100)
    private String title;

    @NotBlank
    @Size(min = 10)
    private String description;

    @NotBlank
    private String color;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.IN_PROGRESS; // Valeur par défaut

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Version
    private Long version; // Pour gérer le versionnement pour la gestion des conflits de modification concurrente

    @Transient
    @CleEtrangere(targetEntity = User.class, entityName = "User")
    private Long userId;

    public Long userId() {
        return userId;
    }

    public Task setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String title() {
        return title;
    }

    public Task setTitle(String title) {
        this.title = title;
        return this;
    }

    public String description() {
        return description;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDateTime dueDate() {
        return dueDate;
    }

    public Task setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskStatus status() {
        return status;
    }

    public Task setStatus(TaskStatus status) {
        this.status = status;
        return this;
    }

    public User user() {
        return user;
    }

    public Task setUser(User user) {
        this.user = user;
        return this;
    }

    public Long version() {
        return version;
    }


    public Task setVersion(Long version) {
        this.version = version;
        return this;
    }

    public String color() {
        return color;
    }

    public Task setColor(String color) {
        this.color = color;
        return this;
    }
}
