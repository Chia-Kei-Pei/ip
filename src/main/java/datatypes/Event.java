package datatypes;

public class Event extends Todo {
    protected String fromDate;
    protected String toDate;

    public Event(String item, String fromDate, String toDate) {
        super("event", item);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return String.format("[E][%s] %s (from: %s to: %s)", isMark ? "X": " ", item, fromDate, toDate);
    }
}
