package com.example.tasks.controller;

import com.example.tasks.model.Task;
import com.example.tasks.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@DisplayName("TaskController Integration Tests")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task("Deploy to production", "Release version 2.0");
        sampleTask.setStatus(Task.TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("GET /api/tasks - should return list of tasks")
    void shouldReturnAllTasks() throws Exception {
        when(taskService.findAll()).thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Deploy to production"))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - should return task when found")
    void shouldReturnTaskById() throws Exception {
        when(taskService.findById(1L)).thenReturn(Optional.of(sampleTask));

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Deploy to production"));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - should return 404 when not found")
    void shouldReturn404WhenTaskNotFound() throws Exception {
        when(taskService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/tasks - should create task and return 201")
    void shouldCreateTask() throws Exception {
        when(taskService.create(any(Task.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Deploy to production"));
    }

    @Test
    @DisplayName("POST /api/tasks - should return 400 when title is blank")
    void shouldReturn400WhenTitleIsBlank() throws Exception {
        Task invalidTask = new Task();
        invalidTask.setTitle("");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} - should update and return task")
    void shouldUpdateTask() throws Exception {
        when(taskService.update(eq(1L), any(Task.class))).thenReturn(Optional.of(sampleTask));

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Deploy to production"));
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - should return 204 on success")
    void shouldDeleteTask() throws Exception {
        when(taskService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - should return 404 when task not found")
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        when(taskService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/tasks?status=PENDING - should filter by status")
    void shouldFilterByStatus() throws Exception {
        when(taskService.findByStatus(Task.TaskStatus.PENDING)).thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/api/tasks?status=PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
