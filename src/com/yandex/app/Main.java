package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.*;

public class Main {

    public static void main(String[] args) {

        HistoryManager historyManager = ManagerFactory.getHistoryManager();
        TaskManager taskManager = ManagerFactory.getTaskManager(historyManager);


        Task task1 = new Task("Hop", "Hiphop");
        taskManager.addTask(task1);
//        taskManager.getTaskByID(task1.getId());

        Task task2 = new Task("Name", "Take name");
        taskManager.addTask(task2);

//        System.out.println(taskManager.takeTasks());
        taskManager.getTaskByID(task2.getId());
        taskManager.getTaskByID(task2.getId());
        taskManager.getTaskByID(task2.getId());
        taskManager.getTaskByID(task1.getId());
        taskManager.getTaskByID(task1.getId());
        taskManager.getTaskByID(task2.getId());

//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task1.getId());
        System.out.println("------- ВЫВОД ");
        System.out.println(historyManager.getHistory());

        System.out.println("-------");




//        InMemoryTaskManager taskManager = new InMemoryTaskManager();
//

        Epic epic1 = new Epic("Shopping", "Buy milk");
        taskManager.addEpic(epic1);
//
        Subtask subtask1 = new Subtask("Go to shop", "with car", epic1.getId());
        taskManager.addSubTask(subtask1);

    }
}
