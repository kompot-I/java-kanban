package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.List;

public interface TaskManager {
    // Метод для добавления таски
    void addTask(Task task);

    // Метод для добавления подзадач
    void addSubTask(Subtask subtask);

    // Метод для добавления эпика
    void addEpic(Epic epic);

    //Обновление задачи
    void updateTask(Task task);

    //Обновление подзадачи
    void updateSubtask(Subtask subtask);

    // Обновление эпика
    void updateEpic(Epic epic);

    //Удаление всех задач.
    void deleteAllTasks();

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    //Получение по идентификатору.
    Task getTaskByID(int id);

    //Удаление по идентификатору.
    void deleteTaskByID(int id);

    void deleteSubtaskById(int id);

    void deleteEpicTasksById(int id);

    //Получение списка всех задач.
    List<Task> takeAllTasks();

    List<Task> takeTasks();

    List<Task> takeSubtasks();

    List<Task> takeEpicTasks();

    //Получение списка всех подзадач определённого эпика.
    List<Subtask> getSubtaskOfEpic(int epicId);
}
