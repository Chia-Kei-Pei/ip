package datatypes;

import parser.DateTimeParser;

import java.time.LocalDateTime;

/**
 * Represents an event task occurring within a specific time period.
 */
public class Event extends Todo {
    protected LocalDateTime fromDate;
    protected LocalDateTime toDate;

    /**
     * Constructs an {@code Event} task with specified completion status, description, start time, and end time.
     *
     * @param isMark Whether this event task is marked as completed.
     * @param description The description of the event.
     * @param fromDate The starting date or time of the event.
     * @param toDate The ending date or time of the event.
     */
    public Event(Boolean isMark, String description, LocalDateTime fromDate, LocalDateTime toDate) {
        super(isMark, description);
        this.type = "event";
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    /**
     * Constructs an unmarked {@code Event} task with specified description, start time, and end time.
     *
     * @param description The description of the event.
     * @param fromDate The starting date or time of the event.
     * @param toDate The ending date or time of the event.
     */
    public Event(String description, LocalDateTime fromDate, LocalDateTime toDate) {
        this(false, description, fromDate, toDate);
    }

    @Override
    public String toString() {
        return String.format("%s (from: %s to: %s)", super.toString(),
                DateTimeParser.format(fromDate),
                DateTimeParser.format(toDate));
    }

    @Override
    public String toFileFormat() {
        return String.format("%s | %s | %s", super.toFileFormat(),
                DateTimeParser.formatForStorage(fromDate),
                DateTimeParser.formatForStorage(toDate));
    }
}
