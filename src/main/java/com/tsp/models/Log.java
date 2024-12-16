package com.tsp.models;

import com.generic.GenericEntity;
import com.tsp.enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Log extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private String action; // Exemple : "Création", "Modification", "Suppression"

    private LocalDateTime timestamp;

    // Informations supplémentaires concernant la modification
    private String oldTitle;
    private String newTitle;

    private String oldDescription;
    private String newDescription;

    private TaskStatus oldStatus;
    private TaskStatus newStatus;

    private Long oldVersion;
    private Long newVersion;

    // Constructeur
    public Log(Task task, String action, String oldTitle, String newTitle,
               String oldDescription, String newDescription,
               TaskStatus oldStatus, TaskStatus newStatus,
               Long oldVersion, Long newVersion) {
        this.task = task;
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.oldTitle = oldTitle;
        this.newTitle = newTitle;
        this.oldDescription = oldDescription;
        this.newDescription = newDescription;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    public Log() {

    }

    public Task task() {
        return task;
    }

    public Log setTask(Task task) {
        this.task = task;
        return this;
    }

    public String action() {
        return action;
    }

    public Log setAction(String action) {
        this.action = action;
        return this;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }

    public Log setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String oldTitle() {
        return oldTitle;
    }

    public Log setOldTitle(String oldTitle) {
        this.oldTitle = oldTitle;
        return this;
    }

    public String newTitle() {
        return newTitle;
    }

    public Log setNewTitle(String newTitle) {
        this.newTitle = newTitle;
        return this;
    }

    public String oldDescription() {
        return oldDescription;
    }

    public Log setOldDescription(String oldDescription) {
        this.oldDescription = oldDescription;
        return this;
    }

    public String newDescription() {
        return newDescription;
    }

    public Log setNewDescription(String newDescription) {
        this.newDescription = newDescription;
        return this;
    }

    public TaskStatus oldStatus() {
        return oldStatus;
    }

    public Log setOldStatus(TaskStatus oldStatus) {
        this.oldStatus = oldStatus;
        return this;
    }

    public TaskStatus newStatus() {
        return newStatus;
    }

    public Log setNewStatus(TaskStatus newStatus) {
        this.newStatus = newStatus;
        return this;
    }

    public Long oldVersion() {
        return oldVersion;
    }

    public Log setOldVersion(Long oldVersion) {
        this.oldVersion = oldVersion;
        return this;
    }

    public Long newVersion() {
        return newVersion;
    }

    public Log setNewVersion(Long newVersion) {
        this.newVersion = newVersion;
        return this;
    }
}
