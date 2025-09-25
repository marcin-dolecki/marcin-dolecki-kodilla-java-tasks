package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
class TaskControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DbService dbService;

    @MockitoBean
    private TaskMapper taskMapper;

    private final Gson gson = new Gson();

    @Test
    void shouldFetchEmptyTaskList() throws Exception {
        when(dbService.getAllTasks()).thenReturn(List.of());
        when(taskMapper.mapToTaskDtoList(List.of())).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchTaskList() throws Exception {
        List<Task> tasks = List.of(
                new Task(1L, "test1", "content 1"),
                new Task(2L, "test2", "content 2")
        );
        List<TaskDto> tasksDto = List.of(
                new TaskDto(1L, "test1", "content 1"),
                new TaskDto(2L, "test2", "content 2")
        );
        when(dbService.getAllTasks()).thenReturn(tasks);
        when(taskMapper.mapToTaskDtoList(tasks)).thenReturn(tasksDto);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("test1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("content 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    void shouldFetchSingleTask() throws Exception {
        Task task = new Task(1L, "test1", "content 1");
        TaskDto taskDto = new TaskDto(1L, "test1", "content 1");
        when(dbService.getTask(1L)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("test1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("content 1")));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Long taskId = 1L;

        doNothing().when(dbService).deleteTask(taskId);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskDto taskDto = new TaskDto(1L, "Updated Task", "Updated Content");
        Task task = new Task(1L, "Updated Task", "Updated Content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(task);
        when(dbService.saveTask(any(Task.class))).thenReturn(task);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(taskDto);

        String jsonContent = gson.toJson(taskDto);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Updated Task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Updated Content")));
    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskDto taskDto = new TaskDto(1L, "New Task", "New Content");
        Task task = new Task(1L, "New Task", "New Content");

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(task);
        when(dbService.saveTask(any(Task.class))).thenReturn(task);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(taskDto);

        String jsonContent = gson.toJson(taskDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("New Task")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("New Content")));
    }

    @Test
    void shouldReturnNotFoundWhenTaskToDeleteDoesNotExist() throws Exception {
        Long taskId = 1L;

        doThrow(new TaskNotFoundException()).when(dbService).deleteTask(taskId);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldReturnInternalServerErrorWhenDbFails() throws Exception {
        when(dbService.getAllTasks()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(500)); // 500 Internal Server Error
    }

    @Test
    void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        when(dbService.getTask(999L)).thenThrow(new TaskNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/tasks/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404)) // 404 Not Found
                .andExpect(content().string("Task with given id doesn't exist"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidTaskPosted() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().is(400)); // 400 Bad Request
    }
}
