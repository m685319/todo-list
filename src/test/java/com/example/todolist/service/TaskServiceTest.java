package com.example.todolist.service;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testMarkAsCompleted() {
        // given
        var task = new Task();
        task.setId(1L);
        task.setTitle("task1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(task);

        // when
        var result = taskService.markAsCompleted(1L);

        // then
        assertTrue(result.isCompleted());
        assertNotNull(result.getCompletionDate());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetOverdueTasks() {
        // given
        var task1 = new Task();
        task1.setTitle("task1");
        task1.setDueDate(LocalDate.now().minusDays(5));
        var task2 = new Task();
        task2.setTitle("task2");
        task2.setDueDate(LocalDate.now().minusDays(3));
        var task3 = new Task();
        task3.setTitle("task3");
        task3.setDueDate(LocalDate.now().plusDays(1));
        doReturn(List.of(task1, task2)).when(taskRepository)
                .findByCompletedFalseAndDueDateBefore(LocalDate.now());

        // when
        var result = taskService.getOverdueTasks();

        // then
        assertEquals(2, result.size());
        assertEquals("task1", result.get(0).getTitle());
        assertEquals(LocalDate.now().minusDays(5), result.get(0).getDueDate());
        assertEquals("task2", result.get(1).getTitle());
        assertEquals(LocalDate.now().minusDays(3), result.get(1).getDueDate());
    }

    @Test
    void testGetById() {
        // given
        var task = new Task();
        task.setId(1L);
        task.setPriority(Task.Priority.LOW);
        task.setTitle("Title 1");
        task.setDueDate(LocalDate.now().plusDays(3));
        doReturn(Optional.of(task)).when(taskRepository).findById(anyLong());

        // when
        var actual = taskService.getById(1L);

        // then
        assertEquals(1L, actual.getId());
        assertEquals(Task.Priority.LOW, actual.getPriority());
        assertEquals("Title 1", actual.getTitle());
    }

    @Test
    @DisplayName("Тест для проверки проставления флага 'archieved' = true")
    void testArchiveTasksBeforeDate() {
        // given
        var task1 = new Task();
        task1.setId(1L);
        task1.setArchived(false);
        var task2 = new Task();
        task2.setId(2L);
        task2.setArchived(false);
        doReturn(List.of(task1, task2)).when(taskRepository)
                .findByCompletedTrueAndCompletionDateBefore(any(LocalDate.class));

        // when
        taskService.archiveTasksBeforeDate(LocalDate.now());

        // then
        verify(taskRepository).saveAll(anyList());
        assertTrue(task1.isArchived());
        assertTrue(task2.isArchived());
    }

    @Test
    void testUnarchiveTask() {
        // given
        var task1 = new Task();
        task1.setId(1L);
        task1.setArchived(true);
        doReturn(Optional.of(task1)).when(taskRepository).findById(1L);

        // when
        taskService.unarchiveTask(1L);

        // then
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task1);
        assertFalse(task1.isArchived());
    }

    @Test
    void testUpdateTaskPriority_taskFound() {
        // given
        var task = new Task();
        task.setId(1L);
        task.setPriority(Task.Priority.LOW);
        doReturn(Optional.of(task)).when(taskRepository).findById(anyLong());

        // when
        taskService.updateTaskPriority(1L, Task.Priority.HIGH);

        // then
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
        assertEquals(Task.Priority.HIGH, task.getPriority());
    }

    @Test
    void testUpdateTaskPriority_taskNotFound() {
        // given
        doReturn(Optional.empty()).when(taskRepository).findById(anyLong());

        // when & then
        var exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTaskPriority(1L, Task.Priority.LOW));
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void testGetTasksBeforeDate() {
        // given
        var task1 = new Task();
        task1.setId(1L);
        task1.setCompleted(true);
        task1.setDueDate(LocalDate.of(2024, 11, 13));
        var task2 = new Task();
        task2.setId(2L);
        task2.setCompleted(true);
        task2.setDueDate(LocalDate.of(2024, 11, 10));
        var task3 = new Task();
        task3.setId(3L);
        task3.setCompleted(false);
        task3.setDueDate(LocalDate.of(2024, 11, 19));
        doReturn(List.of(task1, task2)).when(taskRepository).findTaskByCompletedAndDueDateBefore(true, LocalDate.of(2024, 11, 15));

        // when
        var result = taskService.getTasksBeforeDate(true, LocalDate.of(2024, 11, 15));

        // then
        verify(taskRepository).findTaskByCompletedAndDueDateBefore(true, LocalDate.of(2024, 11, 15));
        assertEquals(2, result.size());
    }
}