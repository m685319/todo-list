package com.example.todolist.controller;

import com.example.todolist.dto.TaskDTO;
import com.example.todolist.service.TaskService;
import jakarta.validation.Valid;
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
    public TaskDTO createTask(@Valid @RequestBody TaskDTO task) {
        return taskService.save(task);
    }

    @GetMapping
    public Page<TaskDTO> getAllTasks(@RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(defaultValue = "dueDate") String sortBy,
                                  @RequestParam(defaultValue = "ASC") String sortDir) {
        return taskService.getAll(page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable long id) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody TaskDTO toUpdate) {
        return taskService.update(id, toUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteById(id);
    }

    @GetMapping("/search")
    public List<TaskDTO> getTaskByTitle(@RequestParam String title) {
        return taskService.getByTitle(title);
    }

    @PutMapping("/{id}/priority")
    public TaskDTO updateTaskPriority(@PathVariable long id, @RequestParam String newPriority) {
        return taskService.updateTaskPriority(id, newPriority);
    }

    @GetMapping("/filter")
    public List<TaskDTO> filterByCompletedAndDueDate(@RequestParam LocalDate dueDate, @RequestParam boolean completed) {
        return taskService.getTasksBeforeDate(completed, dueDate);
    }

    @PutMapping("/{id}/complete")
    public TaskDTO markTaskAsCompleted(@PathVariable Long id) {
        return taskService.markAsCompleted(id);
    }

    @GetMapping("/overdue")
    public List<TaskDTO> getOverdueTasks() {
        return taskService.getOverdueTasks();
    }

    @PutMapping("/archive")
    public void archiveTasks(@RequestParam LocalDate beforeDate) {
        taskService.archiveTasksBeforeDate(beforeDate);
    }

    @PutMapping("/{id}/unarchive")
    public void unarchiveTask(@RequestParam Long id) {
        taskService.unarchiveTask(id);
    }
}
