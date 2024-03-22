package service;

import model.Epic;
import model.StatusType;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1000;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private final HashMap<Integer, Epic> epicTasks = new HashMap<>();

    private int makeID() {
        return id++;
    }

    // Метод для добавления таски
    @Override
    public void addTask(Task task) {
        task.setId(makeID());
        tasks.put(task.getId(), task);
    }

    // Метод для добавления подзадач
    @Override
    public void addSubTask(Subtask subtask) {
        // обновление списка подзадач в эпике
        Epic epic = epicTasks.get(subtask.getEpicId());
        if (epic != null) {
            subtask.setId(makeID());
            subTasks.put(subtask.getId(), subtask);

            epic.addSubTaskID(subtask.getId());
            updateEpicStatus(epic);
        } else {
            System.out.println("model.Epic with id " + subtask.getEpicId() + " not found.");
        }
    }

    // Метод для добавления эпика
    @Override
    public void addEpic(Epic epic) {
        epic.setId(makeID());
        epicTasks.put(epic.getId(), epic);
    }

    //Обновление обычной задачи
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    //Обновление сабтаска
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            updateEpicStatus(epicTasks.get(subtask.getEpicId()));
        }
    }

    // Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        Epic otherEpic = epicTasks.get(epic.getId());
        if (otherEpic != null) {
            otherEpic.setName(epic.getName());
            otherEpic.setDescription(epic.getDescription());
        }
    }

    //Удаление всех задач.
    @Override
    public void deleteAllTasks() {
        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
        id = 1000;
    }

    //Удаление обычных задач.
    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    //Удаление сабтасков.
    @Override
    public void deleteSubtasks() {
        subTasks.clear();
        for (Epic epic : epicTasks.values()) {
            epic.clearSubTaskIDs();
            updateEpicStatus(epic);
        }
    }

    //Удаление эпиков.
    @Override
    public void deleteEpics() {
        epicTasks.clear();
        subTasks.clear();
    }

    //Получение по идентификатору.
    @Override
    public Task getTaskByID(final int id) {
        Task task = tasks.get(id);
        if (task != null) {
            return task;
        } else if ((task = epicTasks.get(id)) != null) {
            return task;
        } else if ((task = subTasks.get(id)) != null) {
            return task;
        }

        return null;
    }

    //Удаление обычных задач по идентификатору.
    @Override
    public void deleteTaskByID(final int id) {
        tasks.remove(id);
    }

    //Удаление сабтасков по идентификатору.
    @Override
    public void deleteSubtaskById(final int id) {
        Subtask subtask = subTasks.remove(id);
        if (subtask != null) {
            int epicID = subtask.getEpicId();
            Epic epic = epicTasks.get(epicID);

            epic.getSubTaskIDs().remove(Integer.valueOf(id));
            updateEpicStatus(epic);
        }
    }

    //Удаление эпиков по идентификатору.
    @Override
    public void deleteEpicTasksById(final int id) {
        Epic epic = epicTasks.remove(id);

        if (epic != null) {
            for (Integer subTaskID : epic.getSubTaskIDs()) {
                subTasks.remove(subTaskID);
            }
        }
    }

    //Получение списка всех задач.
    @Override
    public ArrayList<Task> takeAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(subTasks.values());
        allTasks.addAll(epicTasks.values());

        return allTasks;
    }

    //Получение списка обычных задач.
    @Override
    public ArrayList<Task> takeTasks() {
        return new ArrayList<>(tasks.values());
    }

    //Получение списка сабтасков.
    @Override
    public ArrayList<Task> takeSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    //Получение списка эпиков.
    @Override
    public ArrayList<Task> takeEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    //Получение списка всех подзадач определённого эпика.
    @Override
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

    // обновление статуса эпика
    public void updateEpicStatus(Epic epic) {
        if (epic == null) {
            return;
        }

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
