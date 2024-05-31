package com.yandex.app.service;

import com.yandex.app.exceptions.ManagerSaveException;
import com.yandex.app.model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;
    private HistoryManager historyManager;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            file = Files.createTempFile("tasks", ".csv").toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        historyManager = ManagerFactory.getHistoryManager();
        return FileBackedTaskManager.loadFromFile(file, historyManager);
    }

    @Test
    void mustSuccessLoadFromFile() {
        manager.addTask(new Task("task", "1", LocalDateTime.now(), 60));
        Epic epic = new Epic("epic", "2");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "3", epic.getId(), LocalDateTime.now().plusMinutes(60), 60);
        manager.addSubTask(subtask);

        manager.getTaskByID(epic.getId());
        manager.getTaskByID(subtask.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file, historyManager);

        // Проверка на идентичность id до и после загрузки
        assertEquals(loadedManager.id.get(), manager.id.get());
        // Проверка количества задач до и после загрузки
        assertEquals(loadedManager.takeAllTasks().size(), manager.takeAllTasks().size());
        // Проверка количества задач в истории до и после загрузки
        assertEquals(loadedManager.getHistoryTasks().size(), manager.getHistoryTasks().size());
    }

    @Test
    public void testSaveAndLoad() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file, historyManager);

        assertEquals(1, loadedManager.takeTasks().size());
        assertTrue(loadedManager.takeTasks().contains(task));
    }

    @Test
    public void testSaveHistory() {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        Task task2 = new Task("Task 2", "Description 2", LocalDateTime.now().plusMinutes(60), 30);
        manager.addTask(task1);
        manager.addTask(task2);

        manager.getTaskByID(task1.getId());
        manager.getTaskByID(task2.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file, historyManager);

        List<Task> history = loadedManager.getHistoryTasks();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(loadedManager.prioritizedTasks.size(), manager.prioritizedTasks.size());
    }

    @Test
    public void testSaveWithException() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);

        assertThrows(ManagerSaveException.class, () -> {
            Files.delete(file.toPath());
            FileBackedTaskManager.loadFromFile(file, historyManager);
        });
    }

}