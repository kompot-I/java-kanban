package com.yandex.app.service;

import com.yandex.app.exceptions.ManagerSaveException;
import com.yandex.app.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        loadFromFile(file, historyManager);
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

    private void save() {
        try {
            String csvData = convertToString();
            System.out.println(csvData);
            Files.writeString(file.toPath(), csvData);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения таски в файл " + e.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) {

        FileBackedTaskManager backManager = new FileBackedTaskManager(historyManager, file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;

            while (reader.ready()) {
                line = reader.readLine();
                if (line.startsWith("id")) { continue; }
                if (line.isEmpty()) { break; }

                Task task = convertFromString(line);
                switch (task.getTaskType()) {
                    case TASK:
                        backManager.tasks.put(task.getId(), task);
                        backManager.incrementId();
                    case SUBTASK:
                        backManager.subTasks.put(task.getId(), (Subtask) task);

                        Epic epic = backManager.epicTasks.get(((Subtask) task).getEpicId());
                        if (epic != null) { epic.addSubTaskID(task.getId()); }

                        backManager.incrementId();
                    case EPIC:
                        backManager.epicTasks.put(task.getId(), (Epic) task);
                        backManager.incrementId();
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки данных из файла: " + e.getMessage());
        }

        // удалить
        System.out.println("-----------");
        System.out.println();
        System.out.println("-----------");

        return backManager;
    }

    private static Task convertFromString(String line) {
        String[] elements = line.split(",");

        switch (TaskType.valueOf(elements[1])) {
            case TASK:
                Task task = new Task(elements[2], elements[4]);
                task.setId(Integer.parseInt(elements[0]));
                task.setStatus(StatusType.valueOf(elements[3]));

                return task;
            case EPIC:
                Epic epic = new Epic(elements[2], elements[4]);
                epic.setId(Integer.parseInt(elements[0]));
                epic.setStatus(StatusType.valueOf(elements[3]));

                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(elements[2], elements[4], Integer.parseInt(elements[5]));
                subtask.setId(Integer.parseInt(elements[0]));
                subtask.setStatus(StatusType.valueOf(elements[3]));

                return subtask;
            default:
                Task emptyTask = null;
                return emptyTask;
        }
    }

    private String convertToString(){
        StringBuilder csvData = new StringBuilder();
        csvData.append("id,type,name,status,description,epic").append(System.lineSeparator());

        for (Task task : takeTasks()) {
            csvData.append(task.getId()).append(",").append(TaskType.TASK)
                    .append(",").append(task.getName()).append(",").append(task.getStatus()).append(",")
                    .append(task.getDescription()).append(System.lineSeparator());
        }

        for (Task task : takeEpicTasks()) {
            csvData.append(task.getId()).append(",").append(TaskType.EPIC)
                    .append(",").append(task.getName()).append(",").append(task.getStatus()).append(",")
                    .append(task.getDescription()).append(System.lineSeparator());
        }
        // добавить айди эпиков из сабтасок
        for (Task sub : takeSubtasks()) {
            Subtask task = (Subtask) sub;
            csvData.append(task.getId()).append(",").append(TaskType.SUBTASK)
                    .append(",").append(task.getName()).append(",").append(task.getStatus()).append(",")
                    .append(task.getDescription()).append(",").append(task.getEpicId()).append(System.lineSeparator());
        }

        return csvData.toString();
    }
}
