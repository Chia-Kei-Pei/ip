package bert.parser;

import bert.datatypes.Deadline;
import bert.datatypes.Event;
import bert.datatypes.Task;
import bert.exceptions.BertException;

import java.time.LocalDateTime;

public class TaskParser {
    public static Task parseTodo(boolean isMarked, String description) throws BertException {
        if (description == null || description.isBlank()) {
            throw new BertException("Failed to create task. Some fields are invalid");
        }
        return new Task(isMarked, description);
    }

    public static Task parseTodo(String description) throws BertException {
        return parseTodo(false, description);
    }

    public static Deadline parseDeadline(boolean isMarked, String description, String byDate) throws BertException {
        if (description == null || description.isBlank()
                || byDate == null || byDate.isBlank()) {
            throw new BertException("Failed to create deadline. Some fields are invalid");
        }
        LocalDateTime parsedByDate = DateTimeParser.parse(byDate);
        return new Deadline(isMarked, description, parsedByDate);
    }

    public static Deadline parseDeadline(String description, String byDate) throws BertException {
        return parseDeadline(false, description, byDate);
    }

    public static Event parseEvent(boolean isMarked, String description, String fromDate, String toDate)
            throws BertException {
        if (description == null || description.isBlank() || fromDate == null || fromDate.isBlank()
                || toDate == null || toDate.isBlank()) {
            throw new BertException("Failed to create event. Some fields are invalid");
        }
        LocalDateTime parsedFromDate = DateTimeParser.parse(fromDate);
        LocalDateTime parsedToDate = DateTimeParser.parse(toDate);
        return new Event(description, parsedFromDate, parsedToDate);
    }

    public static Event parseEvent(String description, String fromDate, String toDate) throws BertException {
        return parseEvent(false, description, fromDate, toDate);
    }
}
