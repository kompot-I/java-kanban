public class Task {
    String name;
    String description;
//    private int id;
    private TypeTask status;

//    public int getId() {
//        return id;
//    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TypeTask.NEW;

    }
}
