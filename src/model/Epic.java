package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTaskIDs;

    public Epic(String name, String description) {
        super(name, description);
        subTaskIDs = new ArrayList<>();
    }

    public List<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void clearSubTaskIDs() {
        subTaskIDs.clear();
    }

    public void addSubTaskID(final int subTaskID) {
        this.subTaskIDs.add(subTaskID);
    }
}
