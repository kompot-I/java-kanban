package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.StatusType;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    protected final AtomicInteger id = new AtomicInteger();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Subtask> subTasks = new HashMap<>();
    protected final Map<Integer, Epic> epicTasks = new HashMap<>();
    public final HistoryManager historyManager;

    public int makeID() {
        return id.incrementAndGet();
    }

    protected void incrementId() { id.incrementAndGet(); }

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
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
            System.out.println("com.yandex.app.model.Epic with id " + subtask.getEpicId() + " not found.");
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
        final Set<Integer> idsSet = new HashSet<>();
        idsSet.addAll(tasks.keySet());
        idsSet.addAll(subTasks.keySet());
        idsSet.addAll(epicTasks.keySet());

        historyManager.removeAll(idsSet);

        tasks.clear();
        subTasks.clear();
        epicTasks.clear();
    }

    //Удаление обычных задач.
    @Override
    public void deleteTasks() {
        Set<Integer> idsSet = new HashSet<>(tasks.keySet());
        historyManager.removeAll(idsSet);
        tasks.clear();
    }

    //Удаление сабтасков.
    @Override
    public void deleteSubtasks() {
        Set<Integer> idsSet = new HashSet<>(subTasks.keySet());
        historyManager.removeAll(idsSet);
        subTasks.clear();
        for (Epic epic : epicTasks.values()) {
            epic.clearSubTaskIDs();
            updateEpicStatus(epic);
        }
    }

    //Удаление эпиков.
    @Override
    public void deleteEpics() {
        Set<Integer> idsSet = new HashSet<>();
        idsSet.addAll(subTasks.keySet());
        idsSet.addAll(epicTasks.keySet());

        historyManager.removeAll(idsSet);

        epicTasks.clear();
        subTasks.clear();
    }

    //Получение по идентификатору.
    @Override
    public Task getTaskByID(final int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);

            return task;
        } else if ((task = epicTasks.get(id)) != null) {
            historyManager.add(task);

            return task;
        } else if ((task = subTasks.get(id)) != null) {
            historyManager.add(task);

            return task;
        }

        return null;
    }

    //Удаление обычных задач по идентификатору.
    @Override
    public void deleteTaskByID(final int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    //Удаление сабтасков по идентификатору.
    @Override
    public void deleteSubtaskById(final int id) {
        Subtask subtask = subTasks.remove(id);
        historyManager.remove(id);
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
        historyManager.remove(id);
        if (epic != null) {
            for (Integer subTaskID : epic.getSubTaskIDs()) {
                historyManager.remove(subTaskID);
                subTasks.remove(subTaskID);
            }
        }
    }

    //Получение списка всех задач.
    @Override
    public List<Task> takeAllTasks() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(subTasks.values());
        allTasks.addAll(epicTasks.values());

        return allTasks;
    }

    //Получение списка обычных задач.
    @Override
    public List<Task> takeTasks() {
        return new ArrayList<>(tasks.values());
    }

    //Получение списка сабтасков.
    @Override
    public List<Task> takeSubtasks() {
        return new ArrayList<>(subTasks.values());
    }

    //Получение списка эпиков.
    @Override
    public List<Task> takeEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    //Получение списка всех подзадач определённого эпика.
    @Override
    public List<Subtask> getSubtaskOfEpic(final int epicId) {
        List<Subtask> subForEpic = new ArrayList<>();
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
    private void updateEpicStatus(Epic epic) {
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
