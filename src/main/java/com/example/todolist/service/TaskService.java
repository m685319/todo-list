package com.example.todolist.service;

import com.example.todolist.dto.TaskDTO;
import com.example.todolist.entity.Task;
import com.example.todolist.mapper.TaskMapper;
import com.example.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDTO save(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        Task saved = taskRepository.save(task);
        return taskMapper.toDTO(saved);
    }

    public Page<TaskDTO> getAll(Integer page, Integer size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Task> taskPage = taskRepository.findAll(pageRequest);
        List<TaskDTO> taskDTOs = taskPage.getContent().stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(taskDTOs, pageRequest, taskPage.getTotalElements());
    }

    public TaskDTO getById(long id) {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        TaskDTO taskDTO = taskMapper.toDTO(task);
        taskDTO.setId(task.getId());
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setPriority(task.getPriority().name()); // String type
        taskDTO.setCompleted(task.isCompleted());
        taskDTO.setDueDate(task.getDueDate());
        taskDTO.setCompletionDate(task.getCompletionDate());
        taskDTO.setArchived(task.isArchived());


        return taskDTO;
    }

    public TaskDTO update(Long id, TaskDTO toUpdate) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setTitle(toUpdate.getTitle());
        task.setDescription(toUpdate.getDescription());
        if (!task.isCompleted() && toUpdate.isCompleted()) {
            task.setCompletionDate(LocalDate.now());
        } else if (task.isCompleted() && !toUpdate.isCompleted()) {
            task.setCompletionDate(null);
        }
        task.setCompleted(toUpdate.isCompleted());
        task.setPriority(Task.Priority.valueOf(toUpdate.getPriority()));
        task.setDueDate(toUpdate.getDueDate());
        return taskMapper.toDTO(task);
    }

    public void deleteById(long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskDTO> getByTitle(String title) {
        List<Task> tasks = taskRepository.findTaskByTitleContaining(title);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO updateTaskPriority(long id, String newPriority) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setPriority(Task.Priority.valueOf(newPriority));
        task = taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    /**
     * Метод получает задачи до указанной даты и с указанным статусом завершения.
     *
     * @param completed - статус завершения задачи.
     * @param date      - верхняя граница для дедлайна задач.
     * @return список найденных задач по указанным парамерам.
     */
    public List<TaskDTO> getTasksBeforeDate(boolean completed, LocalDate date) {
        List<Task> tasks = taskRepository.findTaskByCompletedAndDueDateBefore(completed, date);
        return tasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO markAsCompleted(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (!task.isCompleted()) {
            task.setCompleted(true);
            task.setCompletionDate(LocalDate.now());
        }
        task = taskRepository.save(task);
        return taskMapper.toDTO(task);
    }

    public List<TaskDTO> getOverdueTasks() {
        List<Task> overdueTasks = taskRepository.findByCompletedFalseAndDueDateBefore(LocalDate.now());
        return overdueTasks.stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void archiveTasksBeforeDate(LocalDate date) {
        List<Task> tasksToArchive = taskRepository.findByCompletedTrueAndCompletionDateBefore(date);
        tasksToArchive.forEach(task -> task.setArchived(true));
        taskRepository.saveAll(tasksToArchive);
    }

    public void unarchiveTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (task.isArchived()) {
            task.setArchived(false);
            taskRepository.save(task);
        }
    }
}