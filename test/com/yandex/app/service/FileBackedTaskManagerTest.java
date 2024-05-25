//package com.yandex.app.service;
//
//import com.yandex.app.model.Epic;
//import com.yandex.app.model.Subtask;
//import com.yandex.app.model.Task;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FileBackedTaskManagerTest {
//    @Test
//    void mustSuccessLoadFromFile() {
//        HistoryManager historyManager = ManagerFactory.getHistoryManager();
//        try {
//            File file = File.createTempFile("testFile", ".csv");
//            FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file, historyManager);
//
//            fileManager.addTask(new Task("task", "1", null, 0));
//            Epic epic = new Epic("epic", "2");
//            fileManager.addEpic(epic);
//            Subtask subtask = new Subtask("subtask", "3", epic.getId(), null, 0);
//            fileManager.addSubTask(subtask);
//
//            fileManager.getTaskByID(epic.getId());
//            fileManager.getTaskByID(subtask.getId());
//
//            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file, historyManager);
//
//            // Проверка на идентичность id до и после загрузки
//            assertEquals(loadedManager.id.get(), fileManager.id.get());
//            // Проверка количества задач до и после загрузки
//            assertEquals(loadedManager.takeAllTasks().size(), fileManager.takeAllTasks().size());
//            // Проверка количества задач в истории до и после загрузки
//            assertEquals(loadedManager.getHistoryTasks().size(), fileManager.getHistoryTasks().size());
//
//            file.delete();
//        } catch (IOException e) {
//            System.out.println("Ошибка при создании файла");
//        }
//    }
//
//}


package com.yandex.app.service;

import com.yandex.app.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private File file;
    private FileBackedTaskManager manager;
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() throws IOException {
        file = Files.createTempFile("tasks", ".csv").toFile();
        historyManager = ManagerFactory.getHistoryManager();
        manager = FileBackedTaskManager.loadFromFile(file, historyManager);
    }

    @AfterEach
    public void tearDown() {
        file.delete();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);

        assertEquals(1, manager.takeTasks().size());
        assertTrue(manager.takeTasks().contains(task));
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
    public void testUpdateTask() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);
        task.setName("Updated Task");
        manager.updateTask(task);

        assertEquals("Updated Task", manager.getTaskByID(task.getId()).getName());
    }

    @Test
    public void testDeleteTask() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);
        manager.deleteTaskByID(task.getId());

        assertEquals(0, manager.takeTasks().size());
        assertNull(manager.getTaskByID(task.getId()));
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
}