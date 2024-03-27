package service;

import model.Task;
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
        Task task = new Task("Test addNewTask", "Test addNewTask description");

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(task, history.get(0));
    }

    @Test
    void addTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");

        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void sizeOfHistoryElements10() {
        Task task = new Task("1", "description");
        Task task2 = new Task("2", "d");
        task2.setId(2);
        historyManager.add(task2);

        for (int i = 0; i < 10; i++) {
            historyManager.add(task);
        }

        historyManager.add(task2);

        final List<Task> history = historyManager.getHistory();

        assertTrue(historyManager.getHistory().contains(task2));
        assertEquals(10, history.size(), "размер истории больше 10");
        assertEquals(task, history.get(0), "1-й элемент не удалился");
        assertEquals(task2, history.get(9), "11-й элемент не добавился 10-м");
    }
}