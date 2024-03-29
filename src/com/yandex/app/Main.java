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
        taskManager.getTaskByID(task1.getId());

        Task task2 = new Task("Name", "Take name");
        taskManager.addTask(task2);

        System.out.println(taskManager.takeTasks());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task2.getId());
//        taskManager.getTaskByID(task1.getId());
        System.out.println(historyManager.getHistory());

        System.out.println("-------");




//        InMemoryTaskManager taskManager = new InMemoryTaskManager();
//

        Epic epic1 = new Epic("Shopping", "Buy milk");
        taskManager.addEpic(epic1);
//
        Subtask subtask1 = new Subtask("Go to shop", "with car", epic1.getId());
        taskManager.addSubTask(subtask1);

//        Subtask subtask2 = new Subtask("Find milk", "in shop", epic1.getId());
//        taskManager.addSubTask(subtask2);
//
//        Epic epic2 = new Epic("Learning", "YNDX PRKT");
//        taskManager.addEpic(epic2);
//
//        Subtask subtaskLearn = new Subtask("Test project", "Sprint four", epic2.getId());
//        taskManager.addSubTask(subtaskLearn);
//
//        System.out.println("=========");
//        System.out.println(taskManager.takeAllTasks());
//        System.out.println("=========");

//        task1.setStatus(TypeTask.IN_PROGRESS);
//        taskManager.updateTask(task1);
//        System.out.println(task1);
//
//        System.out.println("=========");

//        subtask1.setStatus(TypeTask.IN_PROGRESS);
//        taskManager.updateTask(subtask1);
//        System.out.println(subtask1);
//        System.out.println(epic1);

//        System.out.println("=========");
//
//        System.out.println(subtaskLearn);
//        subtaskLearn.setStatus(StatusType.IN_PROGRESS);
//        taskManager.updateSubtask(subtaskLearn);
//        System.out.println(subtaskLearn);
//        System.out.println(epic2);
//
//        System.out.println("=========");
//
//        subtaskLearn.setStatus(StatusType.DONE);
//        taskManager.updateSubtask(subtaskLearn);
//        System.out.println(epic2);
//
//        System.out.println("=========");
//
//        System.out.println(taskManager.takeAllTasks());
//        taskManager.deleteTaskByID(task1.getId());
//        System.out.println(taskManager.takeAllTasks());
//
//        System.out.println("=========");
//
//        //taskManager.deleteEpicTasksById(epic2.getId());
//        System.out.println(taskManager.takeAllTasks());
//
//        System.out.println("=========");
//
////        taskManager.deleteAllTasks();
//        taskManager.deleteSubtasks();
//        System.out.println(taskManager.takeAllTasks());
    }
}
