package com.example.todolist.service;

import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
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
}