import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<Task> subTaskInEpic;
    public Epic(String name, String description) {
        super(name, description);
        subTaskInEpic = new ArrayList<>();
    }
}
