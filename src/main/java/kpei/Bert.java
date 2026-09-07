package kpei;

import java.io.InputStream;
import java.io.OutputStream;

import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.exceptions.InvalidIndexException;
import kpei.exceptions.UnknownCommandException;
import kpei.parser.CommandParser;
import kpei.parser.ParsedCommand;
import kpei.parser.TaskParser;
import kpei.storage.Storage;
import kpei.ui.Cli;

/**
 * The main application class for BERT task assistant.
 * Handles user interaction loop, task persistence, and programmatic execution of commands.
 */
public class Bert {

    private final String storageFilePath;
    private final Storage storage;
    private final TaskList taskList;
    private final Cli cli;
    private final boolean isGuiMode;

    /**
     * Constructs a {@code Bert} application instance with the specified task list file path and GUI mode.
     *
     * @param todoListFilePath The file path used for task persistence.
     * @param in Input stream for user commands.
     * @param out Output stream for user responses.
     * @param isGuiMode Whether this instance is running in GUI mode.
     */
    public Bert(String todoListFilePath, InputStream in, OutputStream out, boolean isGuiMode) {
        this.storageFilePath = todoListFilePath;
        this.cli = new Cli(in, out);
        this.storage = new Storage(todoListFilePath);
        this.taskList = new TaskList();
        this.isGuiMode = isGuiMode;
    }

    /**
     * Constructs a {@code Bert} application instance in CLI mode with the specified task list file path.
     *
     * @param todoListFilePath The file path used for task persistence.
     * @param in Input stream for user commands.
     * @param out Output stream for user responses.
     */
    public Bert(String todoListFilePath, InputStream in, OutputStream out) {
        this(todoListFilePath, in, out, false);
    }


    /**
     * Entry point for running the BERT assistant CLI application.
     * Initializes storage, loads saved tasks, and starts the command loop.
     */
    public void run() {
        try {
            storage.load(taskList);
        } catch (BertException e) {
            cli.showWarning(e.getMessage());
        }

        cli.greeting();
        cli.showLine();

        while (true) {
            String userPrompt = cli.userPrompt();
            cli.showLine();

            try {
                ParsedCommand command = CommandParser.parse(userPrompt);

                if (command.isExitCommand()) {
                    cli.farewell();
                    return;
                }

                executeCommand(command);
            } catch (BertException | IllegalArgumentException e) {
                cli.showError(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                cli.showError(e.getMessage());
            } finally {
                cli.showLine();
            }
        }
    }

    /**
     * Executes a parsed command by extracting its arguments and dispatching to the appropriate handler method.
     *
     * @param cmd The parsed command containing command type, arguments, and flags.
     * @throws BertException If an application-level error occurs during execution.
     * @throws IllegalArgumentException If an argument format is invalid.
     */
    private void executeCommand(ParsedCommand cmd)
            throws BertException, IllegalArgumentException {
        switch (cmd.getCommandType()) {
            case "todo" -> addTask(TaskParser.parseTodo(cmd.getArgument()));
            case "deadline" -> addTask(TaskParser.parseDeadline(cmd.getArgument(), cmd.getFlag("by")));
            case "event" -> addTask(TaskParser.parseEvent(cmd.getArgument(),
                    cmd.getFlag("from"), cmd.getFlag("to")));
            case "list" -> handleList();
            case "find" -> handleFind(cmd.getArgument());
            case "mark" -> handleMark(cmd.getArgumentAsInt());
            case "unmark" -> handleUnmark(cmd.getArgumentAsInt());
            case "delete", "remove" -> handleDelete(cmd.getArgumentAsInt());
            default -> throw new UnknownCommandException(cmd.getCommandType());
        }
    }

    /**
     * Adds task of any type to tasklist and calls the UI to print a message.
     *
     * @param task
     */
    private void addTask(Task task) {
        taskList.add(task);
        cli.showMsg("Added " + task.getType());
        cli.showTask(taskList.size(), task);
        saveStorage();
    }

    /**
     * Initializes storage and displays the greeting message for GUI startup.
     */
    public void startGui() {
        try {
            storage.load(taskList);
        } catch (BertException e) {
            cli.showWarning(e.getMessage());
        }
        cli.greeting();
        cli.showLine();
    }

    /**
     * Executes a single command string submitted from the user interface.
     *
     * @param userPrompt The command line entered by the user.
     * @return {@code true} if the command was an exit command, {@code false} otherwise.
     */
    public boolean handleUserCommand(String userPrompt) {
        cli.showLine();
        try {
            ParsedCommand command = CommandParser.parse(userPrompt);

            if (command.isExitCommand()) {
                cli.farewell();
                return true;
            }

            executeCommand(command);
        } catch (BertException | IllegalArgumentException | IndexOutOfBoundsException e) {
            cli.showError(e.getMessage());
        } finally {
            cli.showLine();
        }
        return false;
    }

    /**
     * Programmatically prints all items in the task list.
     *
     */
    private void handleList() {
        if (isGuiMode) {
            cli.showMsg("Displaying List.");
        } else {
            cli.showTodoList(taskList);
        }
    }

    /**
     * Finds and displays tasks matching the specified keyword in their description.
     *
     * @param keyword The keyword to search for.
     */
    private void handleFind(String keyword) {
        TaskList matchingTasks = taskList.find(keyword);
        cli.showFoundTasks(matchingTasks);
    }

    /**
     * Programmatically marks a task at the given 1-based index as completed and saves changes.
     *
     * @param index The 1-based index of the task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleMark(int index) throws InvalidIndexException {
        Task task = taskList.get(index);
        if (task.isMarked()) {
            cli.showMsg("Already marked " + task.getType());
            cli.showTask(index, task);
        } else {
            taskList.mark(index);
            cli.showMsg("Marked " + task.getType());
            cli.showTask(index, task);
            saveStorage();
        }
    }

    /**
     * Programmatically unmarks a task at the given 1-based index and saves changes.
     *
     * @param index The 1-based index of the task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleUnmark(int index) throws InvalidIndexException {
        Task task = taskList.get(index);
        if (!task.isMarked()) {
            cli.showMsg("Already unmarked " + task.getType());
            cli.showTask(index, task);
        } else {
            taskList.unmark(index);
            cli.showMsg("Unmarked " + task.getType());
            cli.showTask(index, task);
            saveStorage();
        }
    }

    /**
     * Programmatically removes a task at the given 1-based index from the list and saves changes.
     *
     * @param index The 1-based index of the task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleDelete(int index) throws InvalidIndexException {
        Task task = taskList.remove(index);
        cli.showMsg("Removed " + task.getType());
        cli.showTask(index, task);
        saveStorage();
    }

    /**
     * Saves the current task list to storage and displays a warning if saving fails.
     */
    private void saveStorage() {
        try {
            storage.save(taskList);
        } catch (BertException e) {
            cli.showWarning(e.getMessage());
        }
    }

    /**
     * Returns the task list managed by this BERT instance.
     *
     * @return The current task list.
     */
    public TaskList getTaskList() {
        return taskList;
    }

    /**
     * Returns the storage file path used for task persistence.
     *
     * @return The storage file path.
     */
    public String getStorageFilePath() {
        return storageFilePath;
    }
}

