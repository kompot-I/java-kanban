package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void createTask() {
        String name = "test task";
        String description = "description";
        Task task = new Task(name, description);

        assertEquals(name, task.getName(), "имена не совпадают");
        assertEquals(description, task.getDescription(), "описания не совпадают");
        assertEquals(StatusType.NEW, task.getStatus(), "Не правильный тип");
    }
}