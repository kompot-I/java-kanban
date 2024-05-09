package com.yandex.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerFactoryTest {

    @Test
    void getHistoryManager() {
        HistoryManager historyManager = ManagerFactory.getHistoryManager();
        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }

    @Test
    void getTaskManager() {
        TaskManager manager = ManagerFactory.getTaskManager();

        assertInstanceOf(InMemoryTaskManager.class, manager);
    }
}