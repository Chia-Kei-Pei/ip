package datatypes;

public class Todo {
    protected final String type;
    protected boolean isMark;
    protected String item;

    /**
     * Constructs a {@code Todo} task with default type "todo".
     *
     * @param item The description of the todo task.
     */
    public Todo(String item) {
        this("todo", item);
    }

    /**
     * Constructs a {@code Todo} task with a specified type and description.
     * Used by subclasses (e.g., {@code Deadline}, {@code Event}) to set their type.
     *
     * @param type The type of task (e.g. "todo", "deadline", "event").
     * @param item The description of the task.
     */
    protected Todo(String type, String item) {
        this.type = type;
        this.isMark = false;
        this.item = item;
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
        return String.format("[T][%s] %s", isMark ? "X": " ", item);
    }
}