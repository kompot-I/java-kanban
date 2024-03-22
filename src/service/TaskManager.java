package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    // Метод для добавления таски
    void addTask(Task task);

    // Метод для добавления подзадач
    void addSubTask(Subtask subtask);

    // Метод для добавления эпика
    void addEpic(Epic epic);

    //Обновление задачи или подзадачи
    void updateTask(Task task);

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
    ArrayList<Task> takeAllTasks();

    ArrayList<Task> takeTasks();

    ArrayList<Task> takeSubtasks();

    ArrayList<Task> takeEpicTasks();

    //Получение списка всех подзадач определённого эпика.
    ArrayList<Subtask> getSubtaskOfEpic(int epicId);
}
