package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InMemoryTaskManagerTest {
    private final HistoryManager historyManager = mock(HistoryManager.class);
    private final InMemoryTaskManager taskManager = new InMemoryTaskManager(historyManager);

    private Epic createEpic() {
        Epic epic = new Epic("Shopping", "Buy milk");
        taskManager.addEpic(epic);

        return epic;
    }

    @Test
    void addTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskByID(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.takeTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addSubTask() {
        Epic epic = createEpic();

        Subtask subtask = new Subtask("Go to shop", "with car", epic.getId());
        taskManager.addSubTask(subtask);

        final int subtaskId = subtask.getId();
        final Subtask savedSubtask = (Subtask) taskManager.getTaskByID(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.takeSubtasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subtask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addEpic() {
        Epic epic = createEpic();

        final int epicId = epic.getId();
        final Epic savedEpic = (Epic) taskManager.getTaskByID(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.takeEpicTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(epic, tasks.get(0), "Задачи не совпадают.");
    }

    // проверить что если нет такого id то возвращается null
    // проверить что вернулась task / epic / subtask
    @Test
    void getTaskByIdReturnsNull() {
        assertNull(taskManager.getTaskByID(-1));
    }

    @Test
    void getTaskByIdReturnsTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);

        assertEquals(task, taskManager.getTaskByID(task.getId()));
        verify(historyManager).add(eq(task));
    }

    @Test
    void getTaskByIdReturnsSubtask() {
        Subtask subtask = mock(Subtask.class);
        when(subtask.getEpicId()).thenReturn(-1);
        taskManager.addTask(subtask);

        Subtask actualSubtask = (Subtask) taskManager.getTaskByID(subtask.getId());

        assertEquals(subtask, actualSubtask);
        assertEquals(-1, actualSubtask.getEpicId());
        verify(historyManager).add(eq(subtask));
    }

    @Test
    void getTaskByIdReturnsEpic() {
        Epic epic = createEpic();

        assertEquals(epic, taskManager.getTaskByID(epic.getId()));
        verify(historyManager).add(eq(epic));
    }

}