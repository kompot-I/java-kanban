package com.yandex.app.service;

import com.yandex.app.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void getHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", null, 0);

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(task, history.get(0));
    }

    @Test
    void addTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", null, 0);

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void add_tasksNotRepeat_addedTwoSameTasks() {
        Task task = new Task("1", "first", null, 0);
        task.setId(1);
        Task task2 = new Task("2", "second", null, 0);
        task2.setId(2);

        historyManager.add(task2);
        for (int i = 0; i < 3; i++) {
            historyManager.add(task);
        }
        historyManager.add(task2);

        final List<Task> history = historyManager.getHistory();

        assertTrue(historyManager.getHistory().contains(task));
        assertTrue(historyManager.getHistory().contains(task2));
        assertEquals(2, history.size(), "тасков >2");
        assertEquals(task2, history.get(history.size() - 1));
    }
}