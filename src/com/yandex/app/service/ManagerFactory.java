package com.yandex.app.service;

import java.io.File;

public class ManagerFactory {

    private ManagerFactory() {
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static TaskManager getFileBackedTaskManager(HistoryManager historyManager) {
        return FileBackedTaskManager.loadFromFile(new File("resources.csv"), historyManager);
    }
}
