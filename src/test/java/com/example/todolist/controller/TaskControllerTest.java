package com.example.todolist.controller;

import com.example.todolist.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TaskRepository taskRepository;

    @Test
    @DisplayName("")
    @Sql(scripts = "classpath:scripts/generate_mock_data_task.sql")
    public void testGetAllTasks_success() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].description").value(startsWith("Description for task")));

        verify(taskRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Get a task by ID")
    @Sql(scripts = "classpath:scripts/generate_mock_data_task.sql")
    public void testGetTask_success() throws Exception {

        long taskId = 1L;

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.description").value(startsWith("Description for task 1")));

        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Get a non-existent task by ID")
    @Sql(scripts = "classpath:scripts/generate_mock_data_task.sql")
    public void testGetTask_notFound() throws Exception {
        // given
        long nonExistentId = 1001L;

        // when & then
        mockMvc.perform(get("/api/tasks/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$").value("Task not found with id: 1001"));

        verify(taskRepository).findById(nonExistentId);
    }

}