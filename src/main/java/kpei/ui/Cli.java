package kpei.ui;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import kpei.Bert;
import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.exceptions.UnknownCommandException;
import kpei.parser.CommandParser;
import kpei.parser.ParsedCommand;
import kpei.parser.TaskParser;

/**
 * Command-line interface for the BERT assistant.
 * Manages the CLI run loop, user prompts, command execution, and formatted console output.
 */
public class Cli {

    private static final String DEFAULT_STORAGE_PATH = "data/todo_list_1.txt";
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

    private final Bert bert;
    private final Scanner scanner;
    private final PrintStream printStream;
    private final boolean isGuiMode;

    /**
     * Constructs a {@code Cli} instance with a specified {@link Bert} instance and I/O streams.
     *
     * @param bert The BERT domain controller instance.
     * @param inputStream Input stream for user commands (nullable in GUI mode).
     * @param outputStream Output stream for user responses.
     * @param isGuiMode Whether this interface is running within a GUI context.
     */
    public Cli(Bert bert, InputStream inputStream, OutputStream outputStream, boolean isGuiMode) {
        this.bert = bert;
        this.scanner = inputStream != null ? new Scanner(inputStream) : null;
        this.printStream = new PrintStream(outputStream);
        this.isGuiMode = isGuiMode;
    }

    /**
     * Constructs a {@code Cli} instance for GUI mode where inputs are passed as event strings.
     *
     * @param bert The BERT domain controller instance.
     * @param outputStream Output stream for displaying responses.
     * @param isGuiMode Whether this interface is running within a GUI context.
     */
    public Cli(Bert bert, OutputStream outputStream, boolean isGuiMode) {
        this(bert, null, outputStream, isGuiMode);
    }

    /**
     * Constructs a {@code Cli} instance in CLI mode with the specified task persistence path.
     *
     * @param todoListFilePath File path used for task storage.
     * @param inputStream Input stream for user commands.
     * @param outputStream Output stream for responses.
     */
    public Cli(String todoListFilePath, InputStream inputStream, OutputStream outputStream) {
        this(new Bert(todoListFilePath), inputStream, outputStream, false);
    }

    /**
     * Constructs a {@code Cli} instance in CLI mode with the default task storage path.
     *
     * @param inputStream Input stream for user commands.
     * @param outputStream Output stream for responses.
     */
    public Cli(InputStream inputStream, OutputStream outputStream) {
        this(DEFAULT_STORAGE_PATH, inputStream, outputStream);
    }

    /**
     * Starts the CLI run loop, continuously prompting for commands until an exit command is received.
     */
    public void run() {
        try {
            bert.load();
        } catch (BertException e) {
            showWarning(e.getMessage());
        }

        greeting();
        showLine();

        while (scanner != null && scanner.hasNextLine()) {
            String userPrompt = userPrompt();
            showLine();

            boolean isExit = executeUserCommand(userPrompt);
            showLine();

            if (isExit) {
                return;
            }
        }
    }

    /**
     * Executes a single user command string, printing responses or errors to the output stream.
     *
     * @param userPrompt The raw command string entered by the user.
     * @return {@code true} if an exit command was executed, {@code false} otherwise.
     */
    public boolean executeUserCommand(String userPrompt) {
        try {
            ParsedCommand command = CommandParser.parse(userPrompt);

            if (command.isExitCommand()) {
                farewell();
                return true;
            }

            executeCommand(command);
        } catch (BertException | IllegalArgumentException | IndexOutOfBoundsException e) {
            showError(e.getMessage());
        }
        return false;
    }

    /**
     * Dispatches a parsed command to the appropriate handler method.
     *
     * @param cmd The parsed command.
     * @throws BertException If an application error occurs.
     */
    private void executeCommand(ParsedCommand cmd) throws BertException {
        switch (cmd.getCommandType()) {
            case "todo" -> handleAdd(TaskParser.parseTodo(cmd.getArgument()));
            case "deadline" -> handleAdd(TaskParser.parseDeadline(cmd.getArgument(), cmd.getFlag("by")));
            case "event" -> handleAdd(TaskParser.parseEvent(cmd.getArgument(),
                    cmd.getFlag("from"), cmd.getFlag("to")));
            case "list" -> handleList();
            case "find" -> handleFind(cmd.getArgument());
            case "mark" -> handleMark(cmd.getArgumentAsInt());
            case "unmark" -> handleUnmark(cmd.getArgumentAsInt());
            case "delete", "remove" -> handleDelete(cmd.getArgumentAsInt());
            default -> throw new UnknownCommandException(cmd.getCommandType());
        }
    }

    private void handleAdd(Task task) throws BertException {
        bert.addTask(task);
        showMsg("Added " + task.getType());
        showTask(bert.getTaskList().size(), task);
    }

    private void handleList() {
        if (isGuiMode) {
            showMsg("Displaying List.");
        } else {
            showTodoList(bert.getTaskList());
        }
    }

    private void handleFind(String keyword) {
        TaskList matchingTasks = bert.findTasks(keyword);
        showFoundTasks(matchingTasks);
    }

    private void handleMark(int index) throws BertException {
        Task task = bert.getTaskList().get(index);
        if (task.isMarked()) {
            showMsg("Already marked " + task.getType());
            showTask(index, task);
        } else {
            Task markedTask = bert.markTask(index);
            showMsg("Marked " + markedTask.getType());
            showTask(index, markedTask);
        }
    }

    private void handleUnmark(int index) throws BertException {
        Task task = bert.getTaskList().get(index);
        if (!task.isMarked()) {
            showMsg("Already unmarked " + task.getType());
            showTask(index, task);
        } else {
            Task unmarkedTask = bert.unmarkTask(index);
            showMsg("Unmarked " + unmarkedTask.getType());
            showTask(index, unmarkedTask);
        }
    }

    private void handleDelete(int index) throws BertException {
        Task removedTask = bert.deleteTask(index);
        showMsg("Removed " + removedTask.getType());
        showTask(index, removedTask);
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
     * Prompts the user for command input and returns the entered string.
     *
     * @return Command string entered by the user.
     */
    public String userPrompt() {
        printStream.print("> ");
        return scanner.nextLine();
    }

    /**
     * Displays a general message to the user.
     *
     * @param msg The message text.
     */
    public void showMsg(String msg) {
        printStream.println(msg);
    }

    /**
     * Displays an error message to the user.
     *
     * @param msg The error message text.
     */
    public void showError(String msg) {
        printStream.println(String.format("ERROR!\n%s", msg));
    }

    /**
     * Displays a warning message to the user.
     *
     * @param msg The warning message text.
     */
    public void showWarning(String msg) {
        printStream.println(String.format("Warning.\n%s", msg));
    }

    /**
     * Displays a single task with its 1-based index.
     *
     * @param index The 1-based index of the task.
     * @param task The task to display.
     */
    public void showTask(int index, Task task) {
        printStream.println(String.format("%d.%s", index, task.toString()));
    }

    /**
     * Displays all tasks in the list, or an empty list notification if no tasks exist.
     *
     * @param taskList The list of tasks to display.
     */
    public void showTodoList(TaskList taskList) {
        if (taskList.isEmpty()) {
            printStream.println("List is empty.");
            return;
        }

        for (int i = 0; i < taskList.size(); i++) {
            printStream.println(String.format("%d.%s", i + 1, taskList.getTodos().get(i).toString()));
        }
    }

    /**
     * Displays all matching tasks found from a search, or a message indicating no matches were found.
     *
     * @param foundTasks The list of matching tasks.
     */
    public void showFoundTasks(TaskList foundTasks) {
        if (foundTasks.isEmpty()) {
            printStream.println("No matching tasks found.");
            return;
        }

        printStream.println("Matching tasks:");
        for (int i = 0; i < foundTasks.size(); i++) {
            printStream.println(String.format("%d.%s", i + 1, foundTasks.getTodos().get(i).toString()));
        }
    }

    /**
     * Returns the {@link Bert} domain controller associated with this interface.
     *
     * @return The BERT instance.
     */
    public Bert getBert() {
        return bert;
    }
}
