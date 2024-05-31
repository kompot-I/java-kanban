package com.yandex.app.service;

import com.yandex.app.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    private HistoryManager historyManager;

    @Override
    protected InMemoryTaskManager createTaskManager() {
        historyManager = ManagerFactory.getHistoryManager();

        return (InMemoryTaskManager) ManagerFactory.getDefault(historyManager);
    }

    @Test
    public void testDeleteTaskById() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);
        manager.deleteTaskByID(task.getId());

        assertEquals(0, manager.takeTasks().size());
        assertNull(manager.getTaskByID(task.getId()));
    }

    @Test
    public void testGetHistoryTasks() {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        Task task2 = new Task("Task 2", "Description 2", LocalDateTime.now().plusHours(1), 30);
        manager.addTask(task1);
        manager.addTask(task2);

        manager.getTaskByID(task1.getId());
        manager.getTaskByID(task2.getId());

        List<Task> history = manager.getHistoryTasks();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        Task task2 = new Task("Task 2", "Description 2", LocalDateTime.now().plusHours(1), 30);
        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size());
        assertEquals(task1, prioritizedTasks.get(0));
        assertEquals(task2, prioritizedTasks.get(1));
    }

    @Test
    public void testTimeOverlayValidatorReturnFalse() {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        Task task2 = new Task("Task 2", "Description 2", LocalDateTime.now(), 30);
        manager.addTask(task1);
        manager.addTask(task2);

        assertEquals(1, manager.takeTasks().size());
        assertNull(manager.getTaskByID(task2.getId()));
    }
}