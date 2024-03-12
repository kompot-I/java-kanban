import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1000;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epicTasks = new HashMap<>();

    // Метод для добавления таски
    public void addTask(Task task) {
        task.setId(makeID());
        tasks.put(task.getId(), task);
    }

    // Метод для добавления подзадач
    public void addSubTask(Subtask subtask) {
        // обновление списка подзадач в эпике
        Epic epic = epicTasks.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(makeID());
            subTasks.put(subtask.getId(), subtask);

            epic.addSubTaskID(subtask.getId());
            updateEpicStatus(epic);
        } else {
            System.out.println("Epic with id " + subtask.getEpicId() + " not found.");
        }
    }

    // Метод для добавления эпика
    public void addEpic(Epic epic) {
        epic.setId(makeID());
        epicTasks.put(epic.getId(), epic);
    }

    //Обновление задачи или подзадачи
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            updateEpicStatus(epicTasks.get(subtask.getEpicId()));
        }
    }

    // Обновление эпика
    public void updateEpic(Epic epic) {
        Epic otherEpic = epicTasks.get(epic.getId());
        if (otherEpic != null) {
            otherEpic.setName(epic.getName());
            otherEpic.setDescription(epic.getDescription());
        }
    }

    //Удаление всех задач.
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
        id = 1000;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        subTasks.clear();
        for (Epic epic : epicTasks.values()) {
            epic.clearSubTaskIDs();
            updateEpicStatus(epic);
        }
    }

    public void deleteEpics() {
        epicTasks.clear();
        subTasks.clear();
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
        }
    }

    public void deleteSubtaskById(final int id) {
        if (subTasks.containsKey(id)) {
            Subtask subtask = subTasks.remove(id);
            int epicID = subtask.getEpicId();
            Epic epic = epicTasks.get(epicID);

            epic.getSubTaskIDs().remove(Integer.valueOf(id));
            updateEpicStatus(epic);
        }
    }

    public void deleteEpicTasksById(final int id) {
        if (epicTasks.containsKey(id)) {
            Epic epic = epicTasks.remove(id);
            for (Integer subTaskID : epic.getSubTaskIDs()) {
                subTasks.remove(subTaskID);
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

    public ArrayList<Task> takeTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());

        return allTasks;
    }

    public ArrayList<Task> takeSubtasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(subTasks.values());

        return allTasks;
    }

    public ArrayList<Task> takeEpicTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
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
                subForEpic.add(subtask);
            }
        }

        return subForEpic;
    }

    private int makeID() {
        return id++;
    }

    // обновление статуса эпика
    private void updateEpicStatus(Epic epic) {
        if (epic == null) { return; }

        if (epic.getSubTaskIDs().isEmpty()) {
            epic.setStatus(StatusType.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subtaskId : epic.getSubTaskIDs()) {
            StatusType subtaskStatus = subTasks.get(subtaskId).getStatus();

            if (subtaskStatus != StatusType.NEW) {
                allNew = false;
            }

            if (subtaskStatus != StatusType.DONE) {
                allDone = false;
            }

            if (!allNew && !allDone) {
                epic.setStatus(StatusType.IN_PROGRESS);
            }
        }

        if (allNew) {
            epic.setStatus(StatusType.NEW);
        } else if (allDone) {
            epic.setStatus(StatusType.DONE);
        } else {
            epic.setStatus(StatusType.IN_PROGRESS);
        }
    }
}
