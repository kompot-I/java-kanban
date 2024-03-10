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
    public void getTaskFromID(final int id) {

    }

    //Создание. Сам объект должен передаваться в качестве параметра.
    public Task createTask(Task task) {
        tasks.put(makeID(), task);

        return null;
    }

    //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    //Удаление по идентификатору.
    public void deleteFromID(final int id) {

    }

    //Дополнительные методы:
    //a. Получение списка всех подзадач определённого эпика.
    public void getSubTaskFromEpic() {

    }
}
