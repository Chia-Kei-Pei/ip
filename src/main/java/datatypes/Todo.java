package datatypes;

public class Todo {
    protected String type;
    protected boolean isMark;
    protected String description;

    /**
     * Constructs a {@code Todo} task with a specified type and description.
     * Used by subclasses (e.g., {@code Deadline}, {@code Event}) to set their type
     * and by Storage class for save and load operations.
     *
     * @param isMark Whether this item is marked or not.
     * @param description The description of the task.
     */
    public Todo(Boolean isMark, String description) {
        this.type = "todo";
        this.isMark = isMark;
        this.description = description;
    }

    /**
     * Constructs a {@code Todo} task with isMark set to default value "false".
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

    public void mark() {
        if (isMark) {
            IO.println("Item already marked.");
        } else {
            isMark = true;
            IO.println("Marked Item.");
        }
    }

    public void unmark() {
        if (!isMark) {
            IO.println("Item already unmarked.");
        } else {
            isMark = false;
            IO.println("Unmarked Item.");
        }
    }

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", type, isMark ? "X": " ", description);
    }
}