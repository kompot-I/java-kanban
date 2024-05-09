package com.yandex.app.model;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        setTaskType(TaskType.SUBTASK);
    }

    public int getEpicId() {
        return epicId;
    }
}
