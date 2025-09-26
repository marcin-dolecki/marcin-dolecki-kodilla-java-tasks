package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private DbService dbService;

    @Mock
    private TaskMapper taskMapper;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        TaskController taskController = new TaskController(dbService, taskMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setControllerAdvice(new GlobalHttpErrorHandler())
                .build();
    }

    @Test
    void shouldGetTasks() throws Exception {
        Task task = new Task(1L, "Test task", "content");
        TaskDto taskDto = new TaskDto(1L, "Test task", "content");

        when(dbService.getAllTasks()).thenReturn(List.of(task));
        when(taskMapper.mapToTaskDtoList(List.of(task))).thenReturn(List.of(taskDto));

        mockMvc.perform(get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test task"));
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        when(dbService.getAllTasks()).thenReturn(List.of());
        when(taskMapper.mapToTaskDtoList(List.of())).thenReturn(List.of());

        mockMvc.perform(get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = new Task(2L, "Another task", "content");
        TaskDto taskDto = new TaskDto(2L, "Another task", "content");

        when(dbService.getTask(2L)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        mockMvc.perform(get("/v1/tasks/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.title").value("Another task"));
    }

    @Test
    void shouldReturnBadRequestWhenTaskNotFound() throws Exception {
        when(dbService.getTask(100L)).thenThrow(new TaskNotFoundException());

        mockMvc.perform(get("/v1/tasks/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task with given id doesn't exist"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(dbService).deleteTask(1L);

        mockMvc.perform(delete("/v1/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskDto inputDto = new TaskDto(1L, "Updated", "new content");
        Task mappedTask = new Task(1L, "Updated", "new content");
        Task updatedTask = new Task(1L, "Updated", "new content");
        TaskDto outputDto = new TaskDto(1L, "Updated", "new content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(mappedTask);
        when(dbService.saveTask(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(outputDto);

        mockMvc.perform(put("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void shouldUpdateTaskWithId() throws Exception {
        TaskDto inputDto = new TaskDto(1L, "Updated", "new content");
        Task mappedTask = new Task(1L, "Updated", "new content");
        Task updatedTask = new Task(1L, "Updated", "new content");
        TaskDto outputDto = new TaskDto(1L, "Updated", "new content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(mappedTask);
        when(dbService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(outputDto);

        mockMvc.perform(put("/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskDto inputDto = new TaskDto(1L, "Task", "content");
        Task mappedTask = new Task(1L, "Task", "content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(mappedTask);
        when(dbService.saveTask(mappedTask)).thenReturn(mappedTask);

        mockMvc.perform(post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated());
    }
}
