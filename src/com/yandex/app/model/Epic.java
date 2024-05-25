package com.yandex.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTaskIDs;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, LocalDateTime.of(2024,5,25, 0, 0), 0);
        this.taskType = TaskType.EPIC;
        subTaskIDs = new ArrayList<>();
        this.endTime = LocalDateTime.of(2024,5,25, 0, 0);
    }

    public List<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void clearSubTaskIDs() {
        subTaskIDs.clear();
    }

    public void addSubTaskID(final int subTaskID) {
        this.subTaskIDs.add(subTaskID);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
