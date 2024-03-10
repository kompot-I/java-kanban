import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1000;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epicTasks = new HashMap<>();

    public int makeID() {
        return id++;
    }

    //Получение списка всех задач.
    public ArrayList<Task> takeAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(task);
        }
        for (Task task : subTasks.values()) {
            allTasks.add(task);
        }
        for (Task task : epicTasks.values()) {
            allTasks.add(task);
        }

        return allTasks;
    }

    //Удаление всех задач.
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
        id = 1000;
    }

    //Получение по идентификатору.
    public Task getTaskFromID(final int id) {
        Task task = null;

        if (tasks.containsKey(id)) {
            task = tasks.get(id);
        } else if (epicTasks.containsKey(id)) {
            task = epicTasks.get(id);
        } else if (subTasks.containsKey(id)) {
            task = subTasks.get(id);
        }

        return task;
    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    public void createTask(Task task) {

        if (task.getClass().equals(Task.class)) {
            tasks.put(makeID(), task);
        } else if (task.getClass().equals(Subtask.class)) {
            subTasks.put(makeID(), (Subtask) task);
        } else if (task.getClass().equals(Epic.class)) {
            epicTasks.put(makeID(), (Epic) task);
        }

    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    // в отличии от createTask, не создается новый id для задачи
    public void updateTask(Task task, final int id) {

        if (task.getClass().equals(Task.class)) {
            tasks.put(id, task);
        } else if (task.getClass().equals(Subtask.class)) {
            subTasks.put(id, (Subtask) task);
        } else if (task.getClass().equals(Epic.class)) {
            epicTasks.put(id, (Epic) task);
        }

    }

    //Удаление по идентификатору.
    public void deleteFromID(final int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epicTasks.containsKey(id)) {
            epicTasks.remove(id);
        } else if (subTasks.containsKey(id)) {
            subTasks.remove(id);
        }
    }

    //Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    public ArrayList<Task> getSubTaskFromEpic(final int id) {
        ArrayList<Integer> subFromEpic = new ArrayList<>();
        Epic epic;
        ArrayList<Task> tasksFromEpic = new ArrayList<>();

        // ищем эпик по id и достаем список id его подзадач
        for (Integer epicID : epicTasks.keySet()) {
            if (epicID == id) {
                epic = epicTasks.get(id);
                subFromEpic = epic.subTaskID;
            }
        }

        // состовляем список подзадач по полученому списку ключей
        for (Integer subID : subFromEpic) {
            if (subTasks.containsKey(subID)) {
                tasksFromEpic.add(subTasks.get(subID));
            }
        }

        return tasksFromEpic;
    }
}
