package com.yandex.app.service;

import java.io.File;

public class ManagerFactory {

    private ManagerFactory() {
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTaskManager(HistoryManager historyManager) {
        return new FileBackedTaskManager(new File("resources.csv"));
    }
}
