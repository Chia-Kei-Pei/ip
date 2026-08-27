package datatypes;

/**
 * Represents a task with a deadline date/time constraint.
 */
public class Deadline extends Todo {
    protected String byDate;

    /**
     * Constructs a {@code Deadline} task with specified completion status, description, and due date.
     *
     * @param isMark Whether this deadline task is marked as completed.
     * @param description The description of the deadline task.
     * @param byDate The date or time string by which the task must be completed.
     */
    public Deadline(Boolean isMark, String description, String byDate) {
        super(isMark, description);
        this.type = "deadline";
        this.byDate = byDate;
    }

    /**
     * Constructs an unmarked {@code Deadline} task with specified description and due date.
     *
     * @param description The description of the deadline task.
     * @param byDate The date or time string by which the task must be completed.
     */
    public Deadline(String description, String byDate) {
        this(false, description, byDate);
    }

    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), byDate);
    }
}
