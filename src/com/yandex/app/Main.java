package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.*;

public class Main {

    public static void main(String[] args) {

        HistoryManager historyManager = ManagerFactory.getHistoryManager();
//        TaskManager taskManager = ManagerFactory.getFileBackedTaskManager(historyManager);
        TaskManager taskManager = ManagerFactory.getTaskManager(historyManager);


        // 1
        System.out.println("----1----");
        Epic epicWith3 = new Epic("EpicThree", "go");
        taskManager.addEpic(epicWith3);

        Subtask subtask1 = new Subtask("1", "first", epicWith3.getId());
        taskManager.addSubTask(subtask1);

        Subtask subtask2 = new Subtask("2", "second", epicWith3.getId());
        taskManager.addSubTask(subtask2);

        Subtask subtask3 = new Subtask("3", "third", epicWith3.getId());
        taskManager.addSubTask(subtask3);


        Epic emptyEpic = new Epic("Empty", "zero");
        taskManager.addEpic(emptyEpic);

        // 2 & 3
        System.out.println("----2-&-3----");
        taskManager.getTaskByID(emptyEpic.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getTaskByID(subtask1.getId()); //1
        System.out.println(historyManager.getHistory());
        taskManager.getTaskByID(subtask2.getId()); //2
        System.out.println(historyManager.getHistory());
        taskManager.getTaskByID(subtask1.getId()); //1
        System.out.println(historyManager.getHistory());
        taskManager.getTaskByID(epicWith3.getId());
        System.out.println(historyManager.getHistory());
        taskManager.getTaskByID(subtask3.getId()); //3
        System.out.println(historyManager.getHistory());
        taskManager.getTaskByID(subtask2.getId()); //2
        System.out.println(historyManager.getHistory());

        // 4
        System.out.println("----4----");
        taskManager.deleteSubtaskById(subtask2.getId());
        System.out.println(historyManager.getHistory());

        // 5
        System.out.println("----5----");
        taskManager.deleteEpicTasksById(epicWith3.getId());
        System.out.println(historyManager.getHistory());
    }
}
