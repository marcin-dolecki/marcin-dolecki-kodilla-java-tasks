package com.crud.tasks.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskNotFoundExceptionTest {

    @Test
    void shouldThrowTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> {
            throw new TaskNotFoundException();
        });
    }
}
