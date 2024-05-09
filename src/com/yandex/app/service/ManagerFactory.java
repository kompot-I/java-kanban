package com.yandex.app.service;

import java.io.File;

public class ManagerFactory {

    private ManagerFactory() {
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static TaskManager getFileBackedTaskManager(HistoryManager historyManager) {
        return new FileBackedTaskManager(historyManager, new File("resources.csv"));
    }
}
