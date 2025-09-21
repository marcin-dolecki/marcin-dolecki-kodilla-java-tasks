package com.crud.tasks.service;

import com.crud.tasks.controller.TaskNotFoundException;
import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DbService dbService;

    @Test
    void shouldGetAllTasks() {
        Task task = new Task(1L, "title", "description");
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> tasks = dbService.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("title", tasks.get(0).getTitle());
    }

    @Test
    void shouldGetTaskById() throws TaskNotFoundException {
        Task task = new Task(1L, "title", "description");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = dbService.getTask(1L);

        assertEquals("description", result.getContent());
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> dbService.getTask(1L));
    }

    @Test
    void shouldSaveTask() {
        Task task = new Task(1L, "title", "description");
        when(taskRepository.save(task)).thenReturn(task);

        Task result = dbService.saveTask(task);

        assertEquals("title", result.getTitle());
    }

    @Test
    void shouldUpdateTask() throws TaskNotFoundException {
        Task existing = new Task(1L, "old", "old description");
        Task updated = new Task(2L, "updated", "updated description");
        Task saved = new Task(1L, "updated", "updated description");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        Task result =  dbService.updateTask(1L, updated);

        assertEquals("updated", result.getTitle());
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldDeleteTask() throws TaskNotFoundException {
        when(taskRepository.existsById(1L)).thenReturn(true);

        dbService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingTask() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> dbService.deleteTask(1L));
    }
}
