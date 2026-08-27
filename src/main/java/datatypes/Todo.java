package datatypes;

/**
 * Represents a standard task in the task list.
 * Serves as the base class for specialized task types such as {@link Deadline} and {@link Event}.
 */
public class Todo {
    protected String type;
    protected boolean isMark;
    protected String description;

    /**
     * Constructs a {@code Todo} task with a specified completion status and description.
     * Also used as a base constructor by subclasses (e.g., {@link Deadline}, {@link Event}).
     *
     * @param isMark Whether this task is marked as completed.
     * @param description The description of the task.
     */
    public Todo(Boolean isMark, String description) {
        this.type = "todo";
        this.isMark = isMark;
        this.description = description;
    }

    /**
     * Constructs an unmarked {@code Todo} task with default completion status set to {@code false}.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        this(false, description);
    }

    /**
     * Returns the type of this task.
     *
     * @return The task type string.
     */
    public String getType() {
        return type;
    }

    /**
     * Checks if this task is marked as completed.
     *
     * @return {@code true} if marked, {@code false} otherwise.
     */
    public boolean isMarked() {
        return isMark;
    }

    /**
     * Marks this task as completed.
     */
    public void mark() {
        this.isMark = true;
    }

    /**
     * Unmarks this task, setting its status to not completed.
     */
    public void unmark() {
        this.isMark = false;
    }

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", type, isMark ? "X": " ", description);
    }

    public String toFileFormat() {
        return String.format("%s | %s | %s", type, isMark, description);
    }
}