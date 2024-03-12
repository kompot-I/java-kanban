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

    // Метод для добавления таски
    public void addTask(Task task) {
        task.setId(makeID());
        task.setStatus(TypeTask.NEW);
        tasks.put(task.getId(), task);
    }

    // Метод для добавления подзадач
    public void addSubTask(Subtask subtask) {
        subtask.setId(makeID());
        subtask.setStatus(TypeTask.NEW);
        subTasks.put(subtask.getId(), subtask);

        // обновление списка подзадач в эпике
        Epic epic = epicTasks.get(subtask.getEpicID());
        if (epic != null) {
            epic.addSubTaskID(subtask.getId());
            updateEpicStatus(epic);
        } else {
            System.out.println("Epic with id " + subtask.getEpicID() + " not found.");
        }
    }

    // Метод для добавления эпика
    public void addEpic(Epic epic) {
        epic.setId(makeID());
        epicTasks.put(epic.getId(), epic);
    }

    //Обновление задачи или подзадачи
    public void updateTask(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            if (subTasks.containsKey(subtask.getId())) {
                subTasks.put(subtask.getId(), subtask);
                updateEpicStatus(epicTasks.get(subtask.getEpicID()));
            } else if (tasks.containsKey(task.getId())) {
                tasks.put(task.getId(), task);
            }
        }
    }

    // Обновление эпика
    public void updateEpic(Epic epic) {
        if (epicTasks.containsKey(epic.getId())) {
            Epic otherEpic = epicTasks.get(epic.getId());
            otherEpic.setName(epic.getName());
            otherEpic.setDescription(epic.getDescription());
            updateEpicStatus(otherEpic);
        }
    }

    // обновление статуса эпика
    private void updateEpicStatus(Epic epic) {
        if (epic == null || epic.getSubTaskIDs().isEmpty()) {
            epic.setStatus(TypeTask.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : epic.getSubTaskIDs()) {
            TypeTask subtaskStatus = subTasks.get(subtaskId).getStatus();

            if (subtaskStatus != TypeTask.NEW) {
                allNew = false;
            }

            if (subtaskStatus != TypeTask.DONE) {
                allDone = false;
            }

            if (!allNew && !allDone) {
                epic.setStatus(TypeTask.IN_PROGRESS);
            }
        }

        if (allNew) {
            epic.setStatus(TypeTask.NEW);
        } else if (allDone && !epic.getSubTaskIDs().isEmpty()) {
            epic.setStatus(TypeTask.DONE);
        } else {
            epic.setStatus(TypeTask.IN_PROGRESS);
        }
    }

    //Удаление всех задач.
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
        id = 1000;
    }

    //Получение по идентификатору.
    public Task getTaskByID(final int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epicTasks.containsKey(id)) {
            return epicTasks.get(id);
        } else if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        }

        return null;
    }

    //Удаление по идентификатору.
    public void deleteTaskByID(final int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epicTasks.containsKey(id)) {
            Epic epic = epicTasks.remove(id);
//            epicTasks.remove(id);
            for (Integer subTaskID : epic.getSubTaskIDs()) {
                subTasks.remove(subTaskID);
            }
        } else if (subTasks.containsKey(id)) {
            Subtask subtask = subTasks.remove(id);
            int epicID = subtask.getEpicID();
            Epic epic = epicTasks.get(epicID);

            if (epic != null) {
                epic.getSubTaskIDs().remove(Integer.valueOf(id));
                updateEpicStatus(epic);
            }
        }
    }

    //Получение списка всех задач.
    public ArrayList<Task> takeAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(subTasks.values());
        allTasks.addAll(epicTasks.values());

        return allTasks;
    }

    //Получение списка всех подзадач определённого эпика.
    public ArrayList<Subtask> getSubtaskOfEpic(final int epicId) {
        ArrayList<Subtask> subForEpic = new ArrayList<>();
        Epic epic = epicTasks.get(epicId);

        if (epic != null) {
            for (Integer subtaskId : epic.getSubTaskIDs()) {
                Subtask subtask = subTasks.get(subtaskId);
                if (subtask != null) {
                    subForEpic.add(subtask);
                }
            }
        }

        return subForEpic;
    }
}
