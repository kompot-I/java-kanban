package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    @Test
    void mustSuccessLoadFromFile() {
        HistoryManager historyManager = ManagerFactory.getHistoryManager();
        try {
            File file = File.createTempFile("testFile", ".csv");
            FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file, historyManager);

            fileManager.addTask(new Task("task", "1"));
            Epic epic = new Epic("epic", "2");
            fileManager.addEpic(epic);
            Subtask subtask = new Subtask("subtask", "3", epic.getId());
            fileManager.addSubTask(subtask);

            fileManager.getTaskByID(epic.getId());
            fileManager.getTaskByID(subtask.getId());

            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file, historyManager);

            // Проверка на идентичность id до и после загрузки
            assertEquals(loadedManager.id.get(), fileManager.id.get());
            // Проверка количества задач до и после загрузки
            assertEquals(loadedManager.takeAllTasks().size(), fileManager.takeAllTasks().size());
            // Проверка количества задач в истории до и после загрузки
            assertEquals(loadedManager.getHistoryTasks().size(), fileManager.getHistoryTasks().size());

            file.delete();
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
        }
    }

}