package datatypes;

import exceptions.InvalidIndexException;
import java.util.ArrayList;

/**
 * Represents an ordered, 1-based indexed collection of tasks (Todos, Deadlines, Events).
 * Handles adding, retrieving, removing, marking, and unmarking tasks with range validation.
 */
public class TodoList {
    private final ArrayList<Todo> todos;

    /**
     * Initializes an empty todo list.
     */
    public TodoList() {
        this.todos = new ArrayList<>();
    }

    /**
     * Adds a task to the end of the list and prints the confirmation message.
     *
     * @param todo The task to add.
     */
    public void add(Todo todo) {
        todos.add(todo);
        IO.println("Added " + todo.getType());
        IO.println(todo);
    }

    public void addNoPrint(Todo todo) {
        todos.add(todo);
    }

    /**
     * Validates that the specified 1-based index is within valid list bounds.
     *
     * @param index The 1-based index to validate.
     * @throws InvalidIndexException If the index is less than 1 or greater than the number of todos.
     */
    private void validateIndex(int index) throws InvalidIndexException {
        if (index < 1 || index > todos.size()) {
            throw new InvalidIndexException(index, todos.size());
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
        return todos.get(index - 1);
    }

    public ArrayList<Todo> getTodos() {
        return todos;
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
        Todo todo = todos.remove(index - 1);
        IO.println("Removed todo");
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
        Todo todo = get(index);
        todo.mark();
        IO.println(String.format("%d.%s", index, todo.toString()));
        return todo;
    }

    /**
     * Unmarks the task at the specified 1-based index and prints its formatted representation.
     *
     * @param index The 1-based index of the task.
     * @return The unmarked task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public Todo unmark(int index) throws InvalidIndexException {
        Todo todo = get(index);
        todo.unmark();
        IO.println(String.format("%d.%s", index, todo.toString()));
        return todo;
    }

    /**
     * Prints the task at the specified 1-based index prefixed with its index number.
     *
     * @param index The 1-based index of the task to print.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public void printTodo(int index) throws InvalidIndexException {
        IO.println(String.format("%d.%s", index, get(index).toString()));
    }

    /**
     * Returns the total number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int size() {
        return todos.size();
    }

    /**
     * Checks if the list contains no tasks.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return todos.isEmpty();
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

        for (int i = 0; i < todos.size(); i++) {
            IO.println(String.format("%d.%s", i + 1, todos.get(i).toString()));
        }
    }
}
