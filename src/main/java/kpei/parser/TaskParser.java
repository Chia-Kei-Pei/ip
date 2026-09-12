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
     * Checks whether a storage line contains all fields required for a supported task type.
     *
     * @param line A line read from a storage file.
     * @return {@code true} if the line represents a supported task, {@code false} otherwise.
     */
    public static boolean isStoredTask(String line) {
        if (line.isBlank()) {
            return false;
        }

        String[] fields = line.split("\\s*\\|\\s*");
        if (fields.length < 3) {
            return false;
        }

        return switch (fields[0].trim()) {
            case "todo" -> true;
            case "deadline" -> fields.length >= 4;
            case "event" -> fields.length >= 5;
            default -> false;
        };
    }

    /**
     * Parses a supported storage line into its corresponding task.
     *
     * @param line A line read from a storage file.
     * @return The task represented by the storage line.
     * @throws BertException If the stored task has invalid field values or an unsupported type.
     */
    public static Task parseStoredTask(String line) throws BertException {
        if (!isStoredTask(line)) {
            throw new BertException("Unsupported or incomplete stored task");
        }

        String[] fields = line.split("\\s*\\|\\s*");
        String type = fields[0].trim();
        boolean isMarked = Boolean.parseBoolean(fields[1].trim()) || fields[1].trim().equals("1");
        String description = fields[2].trim();

        return switch (type) {
            case "todo" -> parseTask(isMarked, description);
            case "deadline" -> parseDeadline(isMarked, description, fields[3].trim());
            case "event" -> parseEvent(isMarked, description, fields[3].trim(), fields[4].trim());
            default -> throw new BertException("Unsupported stored task type: " + type);
        };
    }

    /**
     * Creates a {@link Task} with the specified completion status and description.
     *
     * @param isMarked Whether the task is marked as completed.
     * @param description The description of the task.
     * @return The created {@link Task} instance.
     * @throws BertException If the description is blank.
     */
    public static Task parseTask(boolean isMarked, String description) throws BertException {
        if (description.isBlank()) {
            throw new BertException("Failed to create task. Some fields are invalid");
        }
        return new Task(isMarked, description);
    }

    /**
     * Creates an unmarked {@link Task} with the specified description.
     *
     * @param description The description of the task.
     * @return The created {@link Task} instance.
     * @throws BertException If the description is blank.
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
        if (description.isBlank() || byDate.isBlank()) {
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
        if (description.isBlank() || fromDate.isBlank() || toDate.isBlank()) {
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
