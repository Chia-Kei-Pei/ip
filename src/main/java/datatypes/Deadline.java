package datatypes;

public class Deadline extends Todo {
    public String byDate;

    public Deadline(String item, String byDate) {
        super(item);
        this.byDate = byDate;
    }
}
