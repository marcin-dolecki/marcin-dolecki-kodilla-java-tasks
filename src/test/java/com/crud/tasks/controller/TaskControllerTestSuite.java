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

import static org.mockito.Mockito.when;

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
}
