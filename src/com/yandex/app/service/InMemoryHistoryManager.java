package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int SIZE_OF_HISTORY = 10;
    private final List<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.size() == SIZE_OF_HISTORY) {
                history.remove(0);
            }
            history.add(task);
        }
    }
}
