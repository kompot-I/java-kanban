import java.util.ArrayList;

public class Epic extends Task {
    TaskManager taskManager = new TaskManager();
//    private int id;
    ArrayList<Integer> subTaskID;

    public Epic(String name, String description) {
        super(name, description);

        subTaskID = new ArrayList<>();
//        id = taskManager.makeID();
    }
}
