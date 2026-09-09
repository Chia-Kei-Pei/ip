package kpei.datatypes;

import java.time.LocalDateTime;

import kpei.parser.DateTimeParser;

/**
 * Represents a task with a deadline date/time constraint.
 */
public class Deadline extends Task {
    protected LocalDateTime byDate;

    /**
     * Constructs a {@code Deadline} task with specified completion status, description, and due date.
     *
     * @param isMarked Whether this deadline task is marked as completed.
     * @param description The description of the deadline task.
     * @param byDate The date or time string by which the task must be completed.
     */
    public Deadline(boolean isMarked, String description, LocalDateTime byDate) {
        super(isMarked, description);
        type = "deadline";
        this.byDate = byDate;
    }

    /**
     * Constructs an unmarked {@code Deadline} task with specified description and due date.
     *
     * @param description The description of the deadline task.
     * @param byDate The date or time string by which the task must be completed.
     */
    public Deadline(String description, LocalDateTime byDate) {
        this(false, description, byDate);
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), DateTimeParser.format(byDate));
    }

    @Override
    public String toFileFormat() {
        return String.format("%s | %s", super.toFileFormat(), DateTimeParser.formatForStorage(byDate));
    }

    /**
     * Returns the due date and time of this deadline.
     *
     * @return The due date and time.
     */
    public LocalDateTime getByDate() {
        return byDate;
    }

    /**
     * Returns a formatted string representation of the due date and time.
     *
     * @return Formatted date string for display.
     */
    public String getFormattedByDate() {
        return DateTimeParser.format(byDate);
    }
}

