package com.yandex.app.service;

import com.yandex.app.model.*;

import java.time.LocalDateTime;

public class TaskCsvConverter {

    public static Task convertFromString(String line) {
        String[] elements = line.split(",");

        switch (TaskType.valueOf(elements[1])) {
            case TASK:
                Task task = new Task(elements[2], elements[4],
                        LocalDateTime.parse(elements[5]), Integer.parseInt(elements[6]));
                task.setId(Integer.parseInt(elements[0]));
                task.setStatus(StatusType.valueOf(elements[3]));

                return task;
            case EPIC:
                Epic epic = new Epic(elements[2], elements[4]);
                epic.setId(Integer.parseInt(elements[0]));
                epic.setStatus(StatusType.valueOf(elements[3]));

                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(elements[2], elements[4], Integer.parseInt(elements[5]),
                        LocalDateTime.parse(elements[6]), Integer.parseInt(elements[7]));
                subtask.setId(Integer.parseInt(elements[0]));
                subtask.setStatus(StatusType.valueOf(elements[3]));

                return subtask;
            default:
                return null;
        }
    }

    public static String convertTaskToString(Task task) {
        StringBuilder csvLine = new StringBuilder();
        return csvLine.append(task.getId()).append(",").append(TaskType.TASK)
                .append(",").append(task.getName()).append(",").append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getStartTime()).append(",").append(task.getDuration().toMinutes())
                .append(System.lineSeparator())
                .toString();
    }

    public static String convertSubtaskToString(Subtask task) {
        StringBuilder csvLine = new StringBuilder();
        return csvLine.append(task.getId()).append(",").append(TaskType.SUBTASK)
                .append(",").append(task.getName()).append(",").append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",").append(task.getEpicId()).append(",")
                .append(task.getStartTime()).append(",").append(task.getDuration().toMinutes())
                .append(System.lineSeparator())
                .toString();
    }

    public static String convertEpicToString(Epic task) {
        StringBuilder csvLine = new StringBuilder();
        return csvLine.append(task.getId()).append(",").append(TaskType.EPIC)
                .append(",").append(task.getName()).append(",").append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getStartTime()).append(",").append(task.getDuration().toMinutes())
                .append(System.lineSeparator())
                .toString();
    }
}
