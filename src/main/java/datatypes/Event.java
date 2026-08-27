package datatypes;

public class Event extends Todo {
    protected String fromDate;
    protected String toDate;

    public Event(Boolean isMark, String description, String fromDate, String toDate) {
        super(isMark, description);
        this.type = "event";
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Event(String description, String fromDate, String toDate) {
        this(false, description, fromDate, toDate);
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", super.toString(), fromDate, toDate);
    }
}
