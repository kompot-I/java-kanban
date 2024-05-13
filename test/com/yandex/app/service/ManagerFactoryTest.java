package com.yandex.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerFactoryTest {
    private HistoryManager historyManager = ManagerFactory.getHistoryManager();

    @Test
    void getHistoryManager() {
        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }

    @Test
    void getTaskManager() {
        TaskManager manager = ManagerFactory.getDefault(historyManager);

        assertInstanceOf(InMemoryTaskManager.class, manager);
    }
}