package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ManagerFactoryTest {

    @Test
    void getHistoryManager() {
        HistoryManager historyManager = ManagerFactory.getHistoryManager();
        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }

    @Test
    void getTaskManager() {
        HistoryManager historyManager = mock(InMemoryHistoryManager.class);
        TaskManager manager = ManagerFactory.getTaskManager(historyManager);

        assertInstanceOf(InMemoryTaskManager.class, manager);
    }
}