package kpei.ui;

import java.util.Scanner;
import java.util.function.Consumer;

import kpei.Bert;
import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.storage.Storage;

/**
 * Command-line interface and presentation layer for the BERT assistant.
 * Handles reading user input from the terminal and formatting messages for display.
 */
public class Cli {

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

    private final Consumer<String> messageConsumer;
    private Bert bert;

    /**
     * Constructs a {@code Cli} instance with standard terminal output.
     */
    public Cli() {
        this(System.out::print);
    }

    /**
     * Constructs a {@code Cli} instance with a custom message consumer.
     *
     * @param messageConsumer Consumer for output messages.
     */
    public Cli(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    /**
     * Sets the {@link Bert} controller instance.
     *
     * @param bert The Bert controller instance.
     */
    public void setBert(Bert bert) {
        this.bert = bert;
    }

    /**
     * Returns the associated {@link Bert} controller instance.
     *
     * @return The Bert instance.
     */
    public Bert getBert() {
        return bert;
    }

    /**
     * Starts the CLI run loop using the configured {@link Bert} controller.
     */
    public void run() {
        if (bert == null) {
            throw new IllegalStateException("Bert controller must be set before calling run().");
        }
        run(bert);
    }

    /**
     * Starts the CLI run loop with the specified {@link Bert} controller.
     *
     * @param bert The Bert controller instance to coordinate command execution.
     */
    public void run(Bert bert) {
        this.bert = bert;

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String userPrompt = scanner.nextLine();

            if (bert.executeUserCommand(userPrompt)) {
                return;
            }
        }
    }

    /**
     * Prints the welcome greeting and banner.
     */
    public void greeting() {
        print(BANNER);
        print("I am  BERT.");
        print("What do you need?");
    }

    /**
     * Prints the farewell message upon exiting.
     */
    public void farewell() {
        print("Goodbye.");
    }

    /**
     * Prints a horizontal separator line.
     */
    public void showLine() {
        print(HORIZONTAL_LINE);
    }

    /**
     * Displays a general message to the user.
     *
     * @param msg The message text.
     */
    public void showMsg(String msg) {
        print(msg);
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg The error message text.
     */
    public void showError(String msg) {
        print(String.format("ERROR!\n%s", msg));
    }

    /**
     * Displays a warning message to the user.
     *
     * @param msg The warning message text.
     */
    public void showWarning(String msg) {
        print(String.format("Warning.\n%s", msg));
    }

    /**
     * Displays a single task with its 1-based index.
     *
     * @param index The 1-based index of the task.
     * @param task The task to display.
     */
    public void showTask(int index, Task task) {
        print(String.format("%d.%s", index, task.toString()));
    }

    /**
     * Displays all tasks in the list, or an empty list notification if no tasks exist.
     *
     * @param taskList The list of tasks to display.
     */
    public void showTodoList(TaskList taskList) {
        if (taskList.isEmpty()) {
            print("List is empty.");
            return;
        }

        for (int i = 0; i < taskList.size(); i++) {
            print(String.format("%d.%s", i + 1, taskList.getTodos().get(i).toString()));
        }
    }

    /**
     * Displays all matching tasks found from a search, or a message indicating no matches were found.
     *
     * @param foundTasks The list of matching tasks.
     */
    public void showFoundTasks(TaskList foundTasks) {
        if (foundTasks.isEmpty()) {
            print("No matching tasks found.");
            return;
        }

        print("Matching tasks:");
        for (int i = 0; i < foundTasks.size(); i++) {
            print(String.format("%d.%s", i + 1, foundTasks.getTodos().get(i).toString()));
        }
    }

    private void print(String message) {
        messageConsumer.accept(message + "\n");
    }
}
