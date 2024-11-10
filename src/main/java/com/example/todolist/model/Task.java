package com.example.todolist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Priority priority;

    private boolean completed;

    private LocalDate dueDate;

    private LocalDate completionDate;

    public void setCompleted(boolean completed) {
        if (!this.completed && completed) {
            completionDate = LocalDate.now();
        }
        this.completed = completed;
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }
}