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

    @Test
    void twoTasksWithOneIdEquals() {
        Task task = new Task("Test name", "Test description");
        task.setId(1);

        Task newTask = new Task("new name", "new description");
        newTask.setId(1);

        assertEquals(task, newTask);
    }
}