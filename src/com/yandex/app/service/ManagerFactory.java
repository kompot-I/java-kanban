package com.yandex.app.service;

public class ManagerFactory {

    private ManagerFactory() {
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }
}
