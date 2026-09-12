package kpei.parser;

import java.time.LocalDateTime;

import kpei.datatypes.Deadline;
import kpei.datatypes.Event;
import kpei.datatypes.Task;
import kpei.exceptions.BertException;

/**
 * Parses raw parameters into specific {@link Task} objects.
 */
public class TaskParser {

    /**
     * Creates a {@link Task} with the specified completion status and description.
     *
     * @param isMarked Whether the task is marked as completed.
     * @param description The description of the task.
     * @return The created {@link Task} instance.
     * @throws BertException If the description is null or blank.
     */
    public static Task parseTask(boolean isMarked, String description) throws BertException {
        if (description == null || description.isBlank()) {
            throw new BertException("Failed to create task. Some fields are invalid");
        }
        return new Task(isMarked, description);
    }

    /**
     * Creates an unmarked {@link Task} with the specified description.
     *
     * @param description The description of the task.
     * @return The created {@link Task} instance.
     * @throws BertException If the description is null or blank.
     */
    public static Task parseTask(String description) throws BertException {
        return parseTask(false, description);
    }

    /**
     * Creates a {@link Deadline} task with the specified completion status, description, and due date.
     *
     * @param isMarked Whether the deadline task is marked as completed.
     * @param description The description of the deadline task.
     * @param byDate The date or time string by which the task must be completed.
     * @return The created {@link Deadline} instance.
     * @throws BertException If any field is invalid or date parsing fails.
     */
    public static Deadline parseDeadline(boolean isMarked, String description, String byDate) throws BertException {
        if (description == null || description.isBlank()
                || byDate == null || byDate.isBlank()) {
            throw new BertException("Failed to create deadline. Some fields are invalid");
        }
        LocalDateTime parsedByDate = DateTimeParser.parse(byDate);
        return new Deadline(isMarked, description, parsedByDate);
    }

    /**
     * Creates an unmarked {@link Deadline} task with the specified description and due date.
     *
     * @param description The description of the deadline task.
     * @param byDate The date or time string by which the task must be completed.
     * @return The created {@link Deadline} instance.
     * @throws BertException If any field is invalid or date parsing fails.
     */
    public static Deadline parseDeadline(String description, String byDate) throws BertException {
        return parseDeadline(false, description, byDate);
    }

    /**
     * Creates an {@link Event} task with the specified completion status, description, start date, and end date.
     *
     * @param isMarked Whether the event task is marked as completed.
     * @param description The description of the event task.
     * @param fromDate The starting date or time string of the event.
     * @param toDate The ending date or time string of the event.
     * @return The created {@link Event} instance.
     * @throws BertException If any field is invalid or date parsing fails.
     */
    public static Event parseEvent(boolean isMarked, String description, String fromDate, String toDate)
            throws BertException {
        if (description == null || description.isBlank() || fromDate == null || fromDate.isBlank()
                || toDate == null || toDate.isBlank()) {
            throw new BertException("Failed to create event. Some fields are invalid");
        }
        LocalDateTime parsedFromDate = DateTimeParser.parse(fromDate);
        LocalDateTime parsedToDate = DateTimeParser.parse(toDate);
        return new Event(isMarked, description, parsedFromDate, parsedToDate);
    }

    /**
     * Creates an unmarked {@link Event} task with the specified description, start date, and end date.
     *
     * @param description The description of the event task.
     * @param fromDate The starting date or time string of the event.
     * @param toDate The ending date or time string of the event.
     * @return The created {@link Event} instance.
     * @throws BertException If any field is invalid or date parsing fails.
     */
    public static Event parseEvent(String description, String fromDate, String toDate) throws BertException {
        return parseEvent(false, description, fromDate, toDate);
    }
}
