package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.List;
import java.util.Set;

public interface HistoryManager {
    List<Task> getHistory();

    void add(Task task);

    void remove(int id);

    void removeAll(Set<Integer> idsSet);
}
