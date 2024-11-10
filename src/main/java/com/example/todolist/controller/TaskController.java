package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task toUpdate) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setTitle(toUpdate.getTitle());
        task.setDescription(toUpdate.getDescription());
        task.setCompleted(toUpdate.isCompleted());
        task.setPriority(toUpdate.getPriority());
        task.setDueDate(toUpdate.getDueDate());
        return taskRepository.save(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskRepository.deleteById(id);
    }

    @GetMapping("/search")
    public List<Task> getTaskByTitle(@RequestParam String title) {
        return taskRepository.findTaskByTitleContaining(title);
    }

    @PutMapping("/{id}/priority")
    public Task updateTaskPriority(@PathVariable long id, @RequestParam Task.Priority newPriority) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setPriority(newPriority);
        return taskRepository.save(task);
    }

    @GetMapping("/filter")
    public List<Task> filterByCompletedAndDueDate(@RequestParam LocalDate dueDate, @RequestParam boolean completed) {
        return taskRepository.findTaskByCompletedAndDueDateBefore(completed, dueDate);
    }
}
