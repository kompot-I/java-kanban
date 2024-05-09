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
        try {
            File file = File.createTempFile("testFile", ".csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

            fileManager.addTask(new Task("task", "1"));
            Epic epic = new Epic("epic", "2");
            fileManager.addEpic(epic);
            Subtask subtask = new Subtask("subtask", "3", epic.getId());
            fileManager.addSubTask(subtask);

            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

            // Проверка на идентичность id до и после загрузки
            assertEquals(loadedManager.id.get(), fileManager.id.get());
            // Проверка количества задач до и после загрузки
            assertEquals(loadedManager.takeAllTasks().size(), fileManager.takeAllTasks().size());

            file.delete();
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
        }
    }

}