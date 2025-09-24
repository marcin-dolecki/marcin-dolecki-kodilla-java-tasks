package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaskController {

    private final DbService service;
    private final TaskMapper taskMapper;

//    @RequestMapping(method = RequestMethod.GET, value = "") --> alternative approach
    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks() {
        List<Task> tasks = service.getAllTasks();
        return ResponseEntity.ok(taskMapper.mapToTaskDtoList(tasks));
    }

//    @GetMapping(value = "{taskId}", produces = MediaType.APPLICATION_JSON_VALUE) --> alternative when we want to specify which response data type is accepted
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) throws TaskNotFoundException {
        return ResponseEntity.ok(taskMapper.mapToTaskDto(service.getTask(taskId)));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) throws TaskNotFoundException {
        service.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        Task savedTask = service.saveTask(task);
        return ResponseEntity.ok(taskMapper.mapToTaskDto(savedTask));
    }

//    Alternative dla PUT --> based on GTP is more REST
    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable Long id,
            @RequestBody TaskDto taskDto
    ) throws TaskNotFoundException {
        Task taskData = taskMapper.mapToTask(taskDto);
        Task updatedTask = service.updateTask(id, taskData);
        return ResponseEntity.ok(taskMapper.mapToTaskDto(updatedTask));
    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Void> createTask(@RequestBody TaskDto taskDto) {
//        Task task = taskMapper.mapToTask(taskDto);
//        service.saveTask(task);
//        return ResponseEntity.ok().build();
//    }

//    Alternative for POST --> based on GTP it is better
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        Task task = taskMapper.mapToTask(taskDto);
        Task savedTask = service.saveTask(task);
        URI location = URI.create("/v1/tasks/" + savedTask.getId());
        return ResponseEntity.created(location).body(taskMapper.mapToTaskDto(savedTask));
    }
}
