package com.example.todolist.repository;

import com.example.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTaskByTitleContaining(String title);

    List<Task> findTaskByCompletedAndDueDateBefore(boolean completed, LocalDate dueDate);

    List<Task> findByCompletionDateBetween(LocalDate dateFrom, LocalDate dateTo);

    List<Task> findByCompletedFalseAndDueDateBefore(LocalDate currentDate);

    List<Task> findByCompletedTrueAndCompletionDateBefore(LocalDate date);
}