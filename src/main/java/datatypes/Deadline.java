package datatypes;

public class Deadline extends Todo {
    protected String byDate;

    public Deadline(String item, String byDate) {
        super(item);
        this.byDate = byDate;
    }

    @Override
    public String toString() {
        return String.format("[D][%s] %s (by: %s)", isMark ? "X": " ", item, byDate);
    }
}
