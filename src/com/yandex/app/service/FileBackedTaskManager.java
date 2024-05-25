package com.yandex.app.service;

import com.yandex.app.exceptions.ManagerSaveException;
import com.yandex.app.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private FileBackedTaskManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(Subtask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteTaskByID(int id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicTasksById(int id) {
        super.deleteEpicTasksById(id);
        save();
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = super.getTaskByID(id);
        save();
        return task;
    }

    private void save() {
        try {
            String csvData = convertToString();
            Files.writeString(file.toPath(), csvData);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения таски в файл " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) {
        FileBackedTaskManager backManager = new FileBackedTaskManager(file, historyManager);
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> history = new ArrayList<>();
        int countId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while (reader.ready()) {
                line = reader.readLine();
                if (line.startsWith("History")) {
                    break;
                }
                if (line.startsWith("id")) {
                    continue;
                }
                if (line.isEmpty()) {
                    break;
                }
                lines.add(line);
            }

            while (reader.ready()) {
                line = reader.readLine();
                if (line.startsWith("id") || line.startsWith("History")) {
                    continue;
                }
                if (line.isEmpty()) {
                    break;
                }

                history.add(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла: " + e.getMessage());
        }

        for (String str : lines) {
            Task task = TaskCsvConverter.convertFromString(str);

            switch (task.getTaskType()) {
                case TASK:
                    backManager.tasks.put(task.getId(), task);
                    backManager.prioritizedTasks.add(task);
                    break;

                case SUBTASK:
                    backManager.subTasks.put(task.getId(), (Subtask) task);
                    backManager.prioritizedTasks.add(task);

                    Epic epic = backManager.epicTasks.get(((Subtask) task).getEpicId());
                    if (epic != null) {
                        epic.addSubTaskID(task.getId());
                    }
                    break;

                case EPIC:
                    backManager.epicTasks.put(task.getId(), (Epic) task);
                    backManager.prioritizedTasks.add(task);
                    break;
            }

            if (countId < task.getId()) {
                countId = task.getId();
            }
        }

        for (String str : history) {
            Task task = TaskCsvConverter.convertFromString(str);
            historyManager.add(task);
        }

        backManager.id.set(countId);
        return backManager;
    }

    private String convertToString() {
        StringBuilder csvData = new StringBuilder();
        csvData.append("id,type,name,status,description,epic").append(System.lineSeparator());

        for (Task task : takeTasks()) {
            csvData.append(TaskCsvConverter.convertTaskToString(task));
        }
        for (Task task : takeEpicTasks()) {
            csvData.append(TaskCsvConverter.convertEpicToString((Epic) task));
        }
        for (Task sub : takeSubtasks()) {
            csvData.append(TaskCsvConverter.convertSubtaskToString((Subtask) sub));
        }

        csvData.append("History").append(System.lineSeparator());
        csvData.append("id,type,name,status,description,epic").append(System.lineSeparator());

        for (Task task : getHistoryTasks()) {
            if (tasks.containsKey(task.getId())) {
                csvData.append(TaskCsvConverter.convertTaskToString(task));
            }
            if (subTasks.containsKey(task.getId())) {
                csvData.append(TaskCsvConverter.convertSubtaskToString((Subtask) task));
            }
            if (epicTasks.containsKey(task.getId())) {
                csvData.append(TaskCsvConverter.convertEpicToString((Epic) task));
            }
        }

        return csvData.toString();
    }
}
