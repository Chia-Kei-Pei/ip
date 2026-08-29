package bert.ui;

import bert.datatypes.Todo;
import bert.datatypes.TodoList;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

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

    private final Scanner scanner;
    private final PrintStream printStream;

    public Ui(InputStream inputStream, OutputStream outputStream) {
        this.scanner = new Scanner(inputStream);
        this.printStream = new PrintStream(outputStream);
    }

    /**
     * Prints the welcome greeting and banner.
     */
    public void greeting() {
        printStream.println(BANNER);
        printStream.println("I am  BERT.");
        printStream.println("What do you need?");
    }

    /**
     * Prints the farewell message upon exiting.
     */
    public void farewell() {
        printStream.println("Goodbye.");
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        printStream.println(HORIZONTAL_LINE);
    }

    /**
     * Prompts the user for command input and returns the trimmed input string.
     *
     * @return Raw command line input entered by the user.
     */
    public String userPrompt() {
        printStream.print("> ");
        return scanner.nextLine();
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg The error message text.
     */
    public void showError(String msg) {
        printStream.println(msg);
    }

    /**
     * Displays a warning message to the user.
     *
     * @param msg The warning message text.
     */
    public void showWarning(String msg) {
        printStream.println(msg);
    }

    /**
     * Displays a confirmation when a task has been successfully added.
     *
     * @param todo The task that was added.
     */
    public void showAdded(Todo todo) {
        printStream.println("Added " + todo.getType());
        printStream.println(todo);
    }

    /**
     * Displays a confirmation when a task has been removed from the list.
     *
     * @param todo The task that was removed.
     */
    public void showRemoved(Todo todo) {
        printStream.println("Removed todo");
        printStream.println(todo);
    }

    /**
     * Displays a confirmation when a task is marked as completed.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that was marked.
     */
    public void showMarked(int index, Todo todo) {
        printStream.println("Marked Item.");
        printStream.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays a notice when a task was already marked as completed.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that is already marked.
     */
    public void showAlreadyMarked(int index, Todo todo) {
        printStream.println("Item already marked.");
        printStream.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays a confirmation when a task is unmarked.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that was unmarked.
     */
    public void showUnmarked(int index, Todo todo) {
        printStream.println("Unmarked Item.");
        printStream.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays a notice when a task was already unmarked.
     *
     * @param index The 1-based index of the task.
     * @param todo The task that is already unmarked.
     */
    public void showAlreadyUnmarked(int index, Todo todo) {
        printStream.println("Item already unmarked.");
        printStream.println(String.format("%d.%s", index, todo.toString()));
    }

    /**
     * Displays all tasks in the list, or an empty list notification if no tasks exist.
     *
     * @param todoList The list of tasks to display.
     */
    public void showTodoList(TodoList todoList) {
        if (todoList.isEmpty()) {
            printStream.println("List is empty.");
            return;
        }

        for (int i = 0; i < todoList.size(); i++) {
            printStream.println(String.format("%d.%s", i + 1, todoList.getTodos().get(i).toString()));
        }
    }
}
