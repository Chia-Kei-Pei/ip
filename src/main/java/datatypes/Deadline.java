package datatypes;

public class Deadline extends Todo {
    protected String byDate;

    public Deadline(Boolean isMark, String description, String byDate) {
        super(isMark, description);
        this.type = "deadline";
        this.byDate = byDate;
    }

    public Deadline(String description, String byDate) {
        this(false, description, byDate);
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), byDate);
    }
}
