package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.StatusType;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    protected abstract T createTaskManager();

    @BeforeEach
    void init() {
        manager = createTaskManager();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);

        assertEquals(1, manager.takeTasks().size());
        assertTrue(manager.takeTasks().contains(task));
    }

    @Test
    public void testAddEpic() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        assertEquals(1, manager.takeEpicTasks().size());
        assertTrue(manager.takeEpicTasks().contains(epic));
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask);

        assertEquals(1, manager.takeSubtasks().size());
        assertTrue(manager.takeSubtasks().contains(subtask));

        assertEquals(1, manager.getSubtaskOfEpic(epic.getId()).size());
        assertTrue(manager.getSubtaskOfEpic(epic.getId()).contains(subtask));
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
    public void testUpdateSubtask() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask);

        subtask.setName("Updated Subtask");
        manager.updateSubtask(subtask);

        assertEquals("Updated Subtask", manager.getTaskByID(subtask.getId()).getName());
    }

    @Test
    public void testUpdateEpicStatusWithNewSubtasks() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description", epic.getId(), LocalDateTime.now().plusMinutes(30), 30);
        manager.addSubTask(subtask2);

        assertEquals(StatusType.NEW, epic.getStatus());

    }

    @Test
    public void testUpdateEpicStatusWithDoneSubtasks() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description", epic.getId(), LocalDateTime.now().plusMinutes(30), 30);
        manager.addSubTask(subtask2);

        subtask1.setStatus(StatusType.DONE);
        subtask2.setStatus(StatusType.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(StatusType.DONE, epic.getStatus());
    }

    @Test
    public void testUpdateEpicStatusWithProgressSubtasks() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description", epic.getId(), LocalDateTime.now().plusMinutes(30), 30);
        manager.addSubTask(subtask2);

        subtask1.setStatus(StatusType.IN_PROGRESS);
        subtask2.setStatus(StatusType.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(StatusType.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testUpdateEpicStatusWithNewAndDoneSubtasks() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description", epic.getId(), LocalDateTime.now().plusMinutes(30), 30);
        manager.addSubTask(subtask2);

        subtask2.setStatus(StatusType.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        assertEquals(StatusType.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testDeleteTaskById() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);
        manager.deleteTaskByID(task.getId());

        assertEquals(0, manager.takeTasks().size());
        assertNull(manager.getTaskByID(task.getId()));
    }

    @Test
    public void testDeleteSubtaskById() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask);
        manager.deleteSubtaskById(subtask.getId());

        assertEquals(0, manager.takeSubtasks().size());
        assertNull(manager.getTaskByID(subtask.getId()));

        assertEquals(0, manager.getSubtaskOfEpic(epic.getId()).size());
    }

    @Test
    public void testDeleteEpicById() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask);
        manager.deleteEpicTasksById(epic.getId());

        assertEquals(0, manager.takeEpicTasks().size());
        assertEquals(0, manager.takeSubtasks().size());
        assertNull(manager.getTaskByID(epic.getId()));
        assertNull(manager.getTaskByID(subtask.getId()));
    }

    @Test
    public void testDeleteAllTasks() {
        Task task = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        manager.addTask(task);
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask);

        manager.deleteAllTasks();

        assertEquals(0, manager.takeAllTasks().size());
        assertNull(manager.getTaskByID(task.getId()));
        assertNull(manager.getTaskByID(epic.getId()));
        assertNull(manager.getTaskByID(subtask.getId()));
    }

    @Test
    public void testDeleteTasks() {
        Task task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), 60);
        Task task2 = new Task("Task 2", "Description 2", LocalDateTime.now().plusHours(1), 30);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.deleteTasks();

        assertEquals(0, manager.takeTasks().size());
        assertEquals(0, manager.getPrioritizedTasks().size());
        assertNull(manager.getTaskByID(task1.getId()));
        assertNull(manager.getTaskByID(task2.getId()));
    }

    @Test
    public void testDeleteSubtasks() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description", epic.getId(), LocalDateTime.now(), 30);
        manager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description", epic.getId(), LocalDateTime.now().plusMinutes(30), 30);
        manager.addSubTask(subtask2);

        System.out.println(manager.getPrioritizedTasks());
        manager.deleteSubtasks();

        assertEquals(0, manager.takeSubtasks().size());
        assertEquals(0, manager.getPrioritizedTasks().size());
    }

    @Test
    public void testDeleteEpics() {
        Epic epic = new Epic("Epic 1", "Epic Description");
        manager.addEpic(epic);

        manager.deleteEpics();

        assertEquals(0, manager.takeEpicTasks().size());
    }
}