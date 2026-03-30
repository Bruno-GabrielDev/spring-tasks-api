package com.example.tasks.service;

import com.example.tasks.model.Task;
import com.example.tasks.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService Unit Tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task("Fix login bug", "Authentication fails on mobile");
        sampleTask.setStatus(Task.TaskStatus.PENDING);
    }

    @Test
    @DisplayName("Should return all tasks")
    void shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));

        List<Task> result = taskService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Fix login bug");
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find task by ID when it exists")
    void shouldFindTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Optional<Task> result = taskService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Fix login bug");
    }

    @Test
    @DisplayName("Should return empty when task ID not found")
    void shouldReturnEmptyWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should create a new task")
    void shouldCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.create(sampleTask);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Fix login bug");
        verify(taskRepository, times(1)).save(sampleTask);
    }

    @Test
    @DisplayName("Should update an existing task")
    void shouldUpdateTask() {
        Task updated = new Task("Fix login bug - resolved", "Issue fixed");
        updated.setStatus(Task.TaskStatus.DONE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updated);

        Optional<Task> result = taskService.update(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Fix login bug - resolved");
        assertThat(result.get().getStatus()).isEqualTo(Task.TaskStatus.DONE);
    }

    @Test
    @DisplayName("Should return empty when updating non-existent task")
    void shouldReturnEmptyWhenUpdatingNonExistentTask() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.update(99L, sampleTask);

        assertThat(result).isEmpty();
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete task when it exists")
    void shouldDeleteExistingTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        boolean deleted = taskService.delete(1L);

        assertThat(deleted).isTrue();
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent task")
    void shouldReturnFalseWhenDeletingNonExistentTask() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        boolean deleted = taskService.delete(99L);

        assertThat(deleted).isFalse();
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should filter tasks by status")
    void shouldFilterTasksByStatus() {
        when(taskRepository.findByStatus(Task.TaskStatus.PENDING)).thenReturn(List.of(sampleTask));

        List<Task> result = taskService.findByStatus(Task.TaskStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Task.TaskStatus.PENDING);
    }
}
