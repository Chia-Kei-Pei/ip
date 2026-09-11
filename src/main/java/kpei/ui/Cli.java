package kpei.ui;

import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

import kpei.CommandCenter;
import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.InvalidIndexException;

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

    private final Consumer<String> output;

    /**
     * Constructs a {@code Cli} instance with a custom message consumer.
     *
     * @param output Consumer for output messages.
     */
    public Cli(Consumer<String> output) {
        this.output = output;
    }

    /**
     * Starts the CLI run loop using the configured {@link CommandCenter} controller.
     */
    public void run(InputStream input, CommandCenter commandCenter) {
        Scanner scanner = new Scanner(input);
        while (true) {
            System.out.print("> ");
            String userPrompt = scanner.nextLine();

            if (commandCenter.executeCommand(userPrompt)) {
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
    public void horizontalLine() {
        print(HORIZONTAL_LINE);
    }

    /**
     * Displays a general message to the user.
     *
     * @param message The message text.
     */
    public void print(String message) {
        output.accept(message + "\n");
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg The error message text.
     */
    public void error(String msg) {
        print(String.format("ERROR!\n%s", msg));
    }

    /**
     * Displays a warning message to the user.
     *
     * @param msg The warning message text.
     */
    public void warning(String msg) {
        print(String.format("Warning.\n%s", msg));
    }

    /**
     * Displays a single task with its 1-based index.
     *
     * @param index The 1-based index of the task.
     * @param task The task to display.
     */
    public void printTask(int index, Task task) {
        print(String.format("%d.%s", index, task.toString()));
    }

    /**
     * Displays all tasks in the list, or an empty list notification if no tasks exist.
     *
     * @param taskList The list of tasks to display.
     */
    public void printList(TaskList taskList) throws InvalidIndexException {
        if (taskList.isEmpty()) {
            print("List is empty.");
            return;
        }

        for (int i = 0; i <= taskList.size(); i++) {
            assert i >=1 && i <= taskList.size() : "i should always be between 1 and the size of the list inclusive";
            printTask(i, taskList.get(i));
        }
    }
}
