package com.crud.tasks.service;

import com.crud.tasks.controller.TaskNotFoundException;
import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbService {

    private final TaskRepository repository;

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task getTask(final Long id) throws TaskNotFoundException {
        return repository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    public Task updateTask(final Long id, Task taskData) throws TaskNotFoundException {
        Task existingTask = repository.findById(id).orElseThrow(TaskNotFoundException::new);
        Task newTask = new Task(
                existingTask.getId(),
                taskData.getTitle(),
                taskData.getContent()
        );
        return repository.save(newTask);
    }

    public Task saveTask(final Task task) {
        return repository.save(task);
    }
}
