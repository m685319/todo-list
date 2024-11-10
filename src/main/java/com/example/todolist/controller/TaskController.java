package com.example.todolist.controller;

import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.save(task);
    }

    @GetMapping
    public Page<Task> getAllTasks(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(defaultValue = "dueDate") String sortBy,
                                  @RequestParam(defaultValue = "ASC") String sortDir) {
        return taskService.getAll(page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task toUpdate) {
        return taskService.update(id, toUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteById(id);
    }

    @GetMapping("/search")
    public List<Task> getTaskByTitle(@RequestParam String title) {
        return taskService.getByTitle(title);
    }

    @PutMapping("/{id}/priority")
    public Task updateTaskPriority(@PathVariable long id, @RequestParam Task.Priority newPriority) {
        return taskService.updateTaskPriority(id, newPriority);
    }

    @GetMapping("/filter")
    public List<Task> filterByCompletedAndDueDate(@RequestParam LocalDate dueDate, @RequestParam boolean completed) {
        return taskService.getTasksBeforeDate(completed, dueDate);
    }
}
