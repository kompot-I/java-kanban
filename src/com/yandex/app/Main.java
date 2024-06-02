package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.FileBackedTaskManager;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.ManagerFactory;
import com.yandex.app.service.TaskManager;

import java.io.File;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = ManagerFactory.getHistoryManager();
        TaskManager taskManager = ManagerFactory.getFileBackedTaskManager(historyManager);

        // 1
        System.out.println("----1----");
        Epic epicWith3 = new Epic("EpicThree", "go");
        taskManager.addEpic(epicWith3);

        Subtask subtask1 = new Subtask("1", "first", epicWith3.getId(),
                LocalDateTime.of(2024, 5, 26, 10, 0), 120);
        taskManager.addSubTask(subtask1);

        // test task
        Subtask subtaskTest = new Subtask("test", "first", epicWith3.getId(),
                LocalDateTime.of(2024, 5, 26, 10, 0), 120);
        taskManager.addSubTask(subtaskTest);

        Subtask subtask2 = new Subtask("2", "second", epicWith3.getId(),
                LocalDateTime.of(2024, 5, 26, 15, 0), 180);
        taskManager.addSubTask(subtask2);

        Subtask subtask3 = new Subtask("3", "third", epicWith3.getId(),
                LocalDateTime.of(2024, 5, 27, 10, 0), 60);
        taskManager.addSubTask(subtask3);


        Epic emptyEpic = new Epic("Empty", "zero");
        taskManager.addEpic(emptyEpic);

        // 2 & 3
        System.out.println("----2-&-3----");
        taskManager.getTaskByID(emptyEpic.getId());
        System.out.println(taskManager.getHistoryTasks());
        taskManager.getTaskByID(subtask1.getId()); //1
        System.out.println(taskManager.getHistoryTasks());
        taskManager.getTaskByID(subtask2.getId()); //2
        System.out.println(taskManager.getHistoryTasks());
        taskManager.getTaskByID(subtask1.getId()); //1
        System.out.println(taskManager.getHistoryTasks());
        taskManager.getTaskByID(epicWith3.getId());
        System.out.println(taskManager.getHistoryTasks());
        taskManager.getTaskByID(subtask3.getId()); //3
        System.out.println(taskManager.getHistoryTasks());
        taskManager.getTaskByID(subtask2.getId()); //2
        System.out.println(taskManager.getHistoryTasks());

        // 4
        System.out.println("----4----");
//        taskManager.deleteSubtaskById(subtask2.getId());
        System.out.println(taskManager.getHistoryTasks());

        // 5
        System.out.println("----5----");
//        taskManager.deleteEpicTasksById(epicWith3.getId());
        System.out.println(taskManager.getHistoryTasks());

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");

        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");

        TaskManager secondManagerTest = FileBackedTaskManager.loadFromFile(new File("resources.csv"), historyManager);

        Task testTask = new Task("test", "hiphop",
                LocalDateTime.of(2024, 5, 26, 10, 0), 120);
        secondManagerTest.addTask(testTask);

        secondManagerTest.addTask(new Task("1", "2",
                LocalDateTime.of(2024, 5, 26, 10, 0), 120));

        System.out.println("History " + secondManagerTest.getHistoryTasks());


        System.out.println();
    }
}
