package service;

import model.Task;
import java.util.List;

public class Managers {
    List<Task> history;

//    public static HistoryManager getDefaultHistory() {
//        return new InMemoryHistoryManager();
//    }

    public TaskManager getDefault() {
        return null;
    }
}
