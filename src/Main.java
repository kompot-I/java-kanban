import model.Epic;
import model.StatusType;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Hop", "Hiphop");
        taskManager.addTask(task1);
        Task task2 = new Task("Name", "Take name");
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Shopping", "Buy milk");
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Go to shop", "with car", epic1.getId());
        taskManager.addSubTask(subtask1);
        Subtask subtask2 = new Subtask("Find milk", "in shop", epic1.getId());
        taskManager.addSubTask(subtask2);

        Epic epic2 = new Epic("Learning", "YNDX PRKT");
        taskManager.addEpic(epic2);

        Subtask subtaskLearn = new Subtask("Test project", "Sprint four", epic2.getId());
        taskManager.addSubTask(subtaskLearn);

        System.out.println("=========");
        System.out.println(taskManager.takeAllTasks());
        System.out.println("=========");

//        task1.setStatus(TypeTask.IN_PROGRESS);
//        taskManager.updateTask(task1);
//        System.out.println(task1);
//
//        System.out.println("=========");

//        subtask1.setStatus(TypeTask.IN_PROGRESS);
//        taskManager.updateTask(subtask1);
//        System.out.println(subtask1);
//        System.out.println(epic1);

        System.out.println("=========");

        System.out.println(subtaskLearn);
        subtaskLearn.setStatus(StatusType.IN_PROGRESS);
        taskManager.updateSubtask(subtaskLearn);
        System.out.println(subtaskLearn);
        System.out.println(epic2);

        System.out.println("=========");

        subtaskLearn.setStatus(StatusType.DONE);
        taskManager.updateSubtask(subtaskLearn);
        System.out.println(epic2);

        System.out.println("=========");

        System.out.println(taskManager.takeAllTasks());
        taskManager.deleteTaskByID(task1.getId());
        System.out.println(taskManager.takeAllTasks());

        System.out.println("=========");

        //taskManager.deleteEpicTasksById(epic2.getId());
        System.out.println(taskManager.takeAllTasks());

        System.out.println("=========");

//        taskManager.deleteAllTasks();
        taskManager.deleteSubtasks();
        System.out.println(taskManager.takeAllTasks());
    }
}
