package com.yandex.app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.app.server.DurationAdapter;
import com.yandex.app.server.LocalDateTimeAdapter;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

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

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
