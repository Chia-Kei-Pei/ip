package datatypes;

public class Event extends Todo {
    public String fromDate;
    public String toDate;

    public Event(String item, String fromDate, String toDate) {
        super(item);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
