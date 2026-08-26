package datatypes;

import exceptions.InvalidIndexException;
import java.util.ArrayList;

/**
 * Represents an ordered, 1-based indexed collection of tasks (Todos, Deadlines, Events).
 * Handles adding, retrieving, removing, marking, and unmarking tasks with range validation.
 */
public class TodoList {
    private final ArrayList<Todo> items;

    /**
     * Initializes an empty todo list.
     */
    public TodoList() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a task to the end of the list and prints the confirmation message.
     *
     * @param item The task to add.
     */
    public void add(Todo item) {
        items.add(item);
        String itemType = (item instanceof Deadline) ? "deadline"
                : (item instanceof Event) ? "event"
                : "todo";
        IO.println("Added " + itemType);
        IO.println(item);
    }

    /**
     * Validates that the specified 1-based index is within valid list bounds.
     *
     * @param index The 1-based index to validate.
     * @throws InvalidIndexException If the index is less than 1 or greater than the number of items.
     */
    private void validateIndex(int index) throws InvalidIndexException {
        if (index < 1 || index > items.size()) {
            throw new InvalidIndexException(index, items.size());
        }
    }

    /**
     * Retrieves the task at the specified 1-based index.
     *
     * @param index The 1-based index of the task.
     * @return The task at the given index.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public Todo get(int index) throws InvalidIndexException {
        validateIndex(index);
        return items.get(index - 1);
    }

    /**
     * Removes and returns the task at the specified 1-based index, printing the confirmation message.
     *
     * @param index The 1-based index of the task to remove.
     * @return The removed task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public Todo remove(int index) throws InvalidIndexException {
        validateIndex(index);
        Todo todo = items.remove(index - 1);
        IO.println("Removed item");
        IO.println(todo);
        return todo;
    }

    /**
     * Marks the task at the specified 1-based index as done and prints its formatted representation.
     *
     * @param index The 1-based index of the task.
     * @return The marked task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public Todo mark(int index) throws InvalidIndexException {
        Todo item = get(index);
        item.mark();
        IO.println(String.format("%d.%s", index, item.toString()));
        return item;
    }

    /**
     * Unmarks the task at the specified 1-based index and prints its formatted representation.
     *
     * @param index The 1-based index of the task.
     * @return The unmarked task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public Todo unmark(int index) throws InvalidIndexException {
        Todo item = get(index);
        item.unmark();
        IO.println(String.format("%d.%s", index, item.toString()));
        return item;
    }

    /**
     * Prints the task at the specified 1-based index prefixed with its index number.
     *
     * @param index The 1-based index of the task to print.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public void printItem(int index) throws InvalidIndexException {
        IO.println(String.format("%d.%s", index, get(index).toString()));
    }

    /**
     * Returns the total number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return items.size();
    }

    /**
     * Checks if the list contains no tasks.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Prints all tasks in the list prefixed with their 1-based index numbers.
     * If the list contains no tasks, prints an informative message.
     */
    public void printList() {
        if (isEmpty()) {
            IO.println("List is empty.");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            IO.println(String.format("%d.%s", i + 1, items.get(i).toString()));
        }
    }
}
