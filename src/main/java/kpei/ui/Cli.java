package kpei.ui;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.exceptions.InvalidIndexException;
import kpei.exceptions.UnknownCommandException;
import kpei.parser.CommandParser;
import kpei.parser.ParsedCommand;
import kpei.parser.TaskParser;
import kpei.storage.Storage;

/**
 * Command-line interface and task coordinator for the BERT assistant.
 * Manages task list data, storage persistence, user prompt loop, and formatted output.
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

    private final Storage storage;
    private final TaskList taskList;
    private final PrintStream printStream;
    private final boolean isGuiMode;

    /**
     * Constructs a {@code Cli} instance for GUI mode with the specified storage, task list, and output stream.
     *
     * @param storage Storage instance used for task persistence.
     * @param taskList Task list holding the user tasks.
     * @param outputStream Output stream for user responses.
     * @param isGuiMode Whether this interface is running within a GUI context.
     */
    public Cli(Storage storage, TaskList taskList, OutputStream outputStream, boolean isGuiMode) {
        this.storage = storage;
        this.taskList = taskList;
        this.printStream = new PrintStream(outputStream);
        this.isGuiMode = isGuiMode;
    }

    /**
     * Loads tasks from storage into the task list.
     *
     * @throws BertException If an error occurs while reading tasks from storage.
     */
    public void loadStorage() throws BertException {
        storage.load(taskList);
    }

    /**
     * Saves the current task list to storage.
     *
     * @throws BertException If an error occurs while writing tasks to storage.
     */
    public void saveStorage() throws BertException {
        storage.save(taskList);
    }

    /**
     * Starts the CLI run loop, continuously prompting for commands until an exit command is received.
     */
    public void run() {
        try {
            loadStorage();
        } catch (BertException e) {
            showWarning(e.getMessage());
        }

        greeting();
        showLine();

        Scanner scanner = new Scanner(System.in);

        while (scanner != null && scanner.hasNextLine()) {
            printStream.print("> ");
            String userPrompt = scanner.nextLine();
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
        taskList.add(task);
        saveStorage();
        showMsg("Added " + task.getType());
        showTask(taskList.size(), task);
    }

    private void handleList() {
        if (isGuiMode) {
            showMsg("Displaying List.");
        } else {
            showTodoList(taskList);
        }
    }

    private void handleFind(String keyword) {
        TaskList matchingTasks = taskList.find(keyword);
        showFoundTasks(matchingTasks);
    }

    private void handleMark(int index) throws InvalidIndexException, BertException {
        Task task = taskList.get(index);
        if (task.isMarked()) {
            showMsg("Already marked " + task.getType());
            showTask(index, task);
        } else {
            taskList.mark(index);
            saveStorage();
            showMsg("Marked " + task.getType());
            showTask(index, task);
        }
    }

    private void handleUnmark(int index) throws InvalidIndexException, BertException {
        Task task = taskList.get(index);
        if (!task.isMarked()) {
            showMsg("Already unmarked " + task.getType());
            showTask(index, task);
        } else {
            taskList.unmark(index);
            saveStorage();
            showMsg("Unmarked " + task.getType());
            showTask(index, task);
        }
    }

    private void handleDelete(int index) throws InvalidIndexException, BertException {
        Task removedTask = taskList.remove(index);
        saveStorage();
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
     * Returns the task list managed by this instance.
     *
     * @return The task list.
     */
    public TaskList getTaskList() {
        return taskList;
    }

    /**
     * Returns the file path used for task storage.
     *
     * @return The storage file path.
     */
    public String getStorageFilePath() {
        return storage.getFilePath();
    }

    /**
     * Returns the {@link Storage} handler used by this instance.
     *
     * @return The storage instance.
     */
    public Storage getStorage() {
        return storage;
    }
}
