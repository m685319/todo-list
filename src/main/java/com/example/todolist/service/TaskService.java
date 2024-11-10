package com.example.todolist.service;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task getById(long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task update(Long id, Task toUpdate) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setTitle(toUpdate.getTitle());
        task.setDescription(toUpdate.getDescription());
        if(!task.isCompleted() && toUpdate.isCompleted()) {
            task.setCompletionDate(LocalDate.now());
        } else if (task.isCompleted() && !toUpdate.isCompleted()) {
            task.setCompletionDate(null);
        }
        task.setCompleted(toUpdate.isCompleted());
        task.setPriority(toUpdate.getPriority());
        task.setDueDate(toUpdate.getDueDate());
        return taskRepository.save(task);
    }

    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getByTitle(String title) {
        return taskRepository.findTaskByTitleContaining(title);
    }

    public Task updateTaskPriority(long id, Task.Priority newPriority) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setPriority(newPriority);
        return taskRepository.save(task);
    }

    public List<Task> getTasksBeforeDate(boolean completed, LocalDate dueDate) {
        return taskRepository.findTaskByCompletedAndDueDateBefore(completed, dueDate);
    }
}