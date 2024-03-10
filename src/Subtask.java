public class Subtask extends Task {
    private int epicID;
    private TypeTask status;
    public Subtask(String name, String description) {
        super(name, description);
    }

    public void setStatus(TypeTask status) {
        this.status = status;
    }
}
