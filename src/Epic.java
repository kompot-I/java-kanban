import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subTaskIDs;

    public Epic(String name, String description) {
        super(name, description);
        subTaskIDs = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void addSubTaskID(final int subTaskID) {
        this.subTaskIDs.add(subTaskID);
    }
}
