package service;

public class ManagerFactory {

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }
}
