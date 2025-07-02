package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final DbService service;
    private final TaskMapper taskMapper;

//    @RequestMapping(method = RequestMethod.GET, value = "") --> alternative approach
    @GetMapping
    public List<TaskDto> getTasks() {
        List<Task> tasks = service.getAllTasks();
        return taskMapper.mapToTaskDtoList(tasks);
    }

//    @GetMapping(value = "{taskId}", produces = MediaType.APPLICATION_JSON_VALUE) --> alternative when we want to specify which response data type is accepted
    @GetMapping("/{taskId}")
    public ResponseEntity <TaskDto> getTask(@PathVariable Long taskId) throws TaskNotFoundException {
        try {
            return new ResponseEntity<>(taskMapper.mapToTaskDto(service.getTask(taskId)), HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(
                    new TaskDto(0L, "There is no task with id equals to " + taskId, ""), HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping
    public void deleteTask(Long taskId) {

    }

    @PutMapping
    public TaskDto updateTask(TaskDto taskDto) {
        return new TaskDto(1L, "Edited test title", "Edited test content");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        service.saveTask(task);
    }
}
