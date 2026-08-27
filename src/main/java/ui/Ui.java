package ui;

import datatypes.Todo;
import datatypes.TodoList;

/**
 * Handles all user interactions and console input/output for BERT assistant.
 */
public class Ui {
    private static final String BANNER = """
 ____     ___  ____  ______
|    \\   /  _]|    \\|      |
|  o  ) /  [_ |  D  )      |
|     ||    _]|    /|_|  |_|
|  O  ||   [_ |    \\  |  | 
|     ||     ||  .  \\ |  | 
|_____||_____||__|\\_| |__| 
""";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    public Ui() {

    }

    /**
     * Prints the welcome greeting and banner.
     */
    public void greeting() {
        IO.println(BANNER);
        IO.println("I am  BERT.");
        IO.println("What do you need?");
    }

    /**
     * Prints the farewell message upon exiting.
     */
    public void farewell() {
        IO.println("Goodbye.");
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        IO.println(HORIZONTAL_LINE);
    }

    /**
     * Prompts the user for command input and returns the trimmed input string.
     *
     * @return Raw command line input entered by the user.
     */
    public String userPrompt() {
        return IO.readln("> ");
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg The error message text.
     */
    public void showError(String msg) {
        IO.println(msg);
    }

    /**
     * Displays a warning message to the user.
     *
     * @param msg The warning message text.
     */
    public void showWarning(String msg) {
        IO.println(msg);
    }

    /**
     * Displays a confirmation when a task has been successfully added.
     *
     * @param todo The task that was added.
     */
    public void showAdded(Todo todo) {
        IO.println("Added " + todo.getType());
        IO.println(todo);
    }

    /**
     * Displays a confirmation when a task has been removed from the list.
     *
     * @param todo The task that was removed.
     */
    public void showRemoved(Todo todo) {
        IO.println("Removed todo");
        IO.println(todo);
    }

    /**
     * Displays a confirmation when a task is marked as completed.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that was marked.
     */
    public void showMarked(int index, Todo todo) {
        IO.println("Marked Item.");
        IO.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays a notice when a task was already marked as completed.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that is already marked.
     */
    public void showAlreadyMarked(int index, Todo todo) {
        IO.println("Item already marked.");
        IO.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays a confirmation when a task is unmarked.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that was unmarked.
     */
    public void showUnmarked(int index, Todo todo) {
        IO.println("Unmarked Item.");
        IO.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays a notice when a task was already unmarked.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that is already unmarked.
     */
    public void showAlreadyUnmarked(int index, Todo todo) {
        IO.println("Item already unmarked.");
        IO.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays all tasks in the list, or an empty list notification if no tasks exist.
     *
     * @param todoList The list of tasks to display.
     */
    public void showTodoList(TodoList todoList) {
        if (todoList.isEmpty()) {
            IO.println("List is empty.");
            return;
        }

        for (int i = 0; i < todoList.size(); i++) {
            IO.println(String.format("%d.%s", i + 1, todoList.getTodos().get(i).toString()));
        }
    }
}
