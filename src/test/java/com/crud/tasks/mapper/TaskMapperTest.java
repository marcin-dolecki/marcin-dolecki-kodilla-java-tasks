package com.crud.tasks.mapper;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void shouldMapToTask() {
        TaskDto taskDto = new TaskDto(1L, "title", "content");

        Task task = taskMapper.mapToTask(taskDto);

        assertEquals(1L, task.getId());
        assertEquals("title", task.getTitle());
        assertEquals("content", task.getContent());
    }

    @Test
    void shouldMapToTaskDto() {
        Task task = new Task(1L, "title", "content");

        TaskDto taskDto = taskMapper.mapToTaskDto(task);

        assertEquals(1L, taskDto.getId());
        assertEquals("title", taskDto.getTitle());
        assertEquals("content", taskDto.getContent());
    }
    
    @Test
    void shouldMapToTaskDtoList() {
        List<Task> tasks = List.of(
                new Task(1L, "title1", "content1"),
                new Task(2L, "title2", "content2")
        );

        List<TaskDto> taskDtos = taskMapper.mapToTaskDtoList(tasks);

        assertEquals(2, taskDtos.size());
        assertEquals(1L, taskDtos.get(0).getId());
        assertEquals("title1", taskDtos.get(0).getTitle());
        assertEquals("content1", taskDtos.get(0).getContent());
        assertEquals(2L, taskDtos.get(1).getId());
        assertEquals("title2", taskDtos.get(1).getTitle());
        assertEquals("content2", taskDtos.get(1).getContent());
    }
}
