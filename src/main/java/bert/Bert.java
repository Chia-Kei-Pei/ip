package bert;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

import bert.datatypes.Deadline;
import bert.datatypes.Event;
import bert.datatypes.Task;
import bert.datatypes.TaskList;
import bert.exceptions.BertException;
import bert.exceptions.InvalidIndexException;
import bert.exceptions.UnknownCommandException;
import bert.parser.CommandParser;
import bert.parser.DateTimeParser;
import bert.parser.ParsedCommand;
import bert.storage.Storage;
import bert.ui.Ui;

/**
 * The main application class for BERT task assistant.
 * Handles user interaction loop, task persistence, and programmatic execution of commands.
 */
public class Bert {

    private Storage storage;
    private TaskList taskList;
    private Ui ui;

    /**
     * Constructs a {@code Bert} application instance with the specified task list file path.
     *
     * @param todoListFilePath The file path used for task persistence.
     * @param in Input stream for user commands.
     * @param out Output stream for user responses.
     */
    public Bert(String todoListFilePath, InputStream in, OutputStream out) {
        ui = new Ui(in, out);
        storage = new Storage(todoListFilePath, ui);
        taskList = new TaskList();
    }

    /**
     * Entry point for running the BERT assistant CLI application.
     * Initializes storage, loads saved tasks, and starts the command loop.
     */
    public void run() {
        storage.load(taskList);

        ui.greeting();
        ui.showLine();

        while (true) {
            String userPrompt = ui.userPrompt();
            ui.showLine();

            try {
                ParsedCommand command = CommandParser.parse(userPrompt);

                if (command.isExitCommand()) {
                    ui.farewell();
                    return;
                }

                executeCommand(command);
            } catch (BertException | IllegalArgumentException e) {
                ui.showError(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
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
            case "todo" -> handleTodo(cmd.getArgument());
            case "deadline" -> handleDeadline(cmd.getArgument(), cmd.getFlag("by"));
            case "event" -> handleEvent(cmd.getArgument(), cmd.getFlag("from"), cmd.getFlag("to"));
            case "list" -> handleList();
            case "find" -> handleFind(cmd.getArgument());
            case "mark" -> handleMark(cmd.getArgumentAsInt());
            case "unmark" -> handleUnmark(cmd.getArgumentAsInt());
            case "delete", "remove" -> handleDelete(cmd.getArgumentAsInt());
            default -> throw new UnknownCommandException(cmd.getCommandType());
        }
    }

    /**
     * Programmatically adds a new {@link Task} task to the list and saves changes.
     *
     * @param description The description of the todo task.
     */
    private void handleTodo(String description) {
        Task todo = new Task(description);
        addTask(todo);
    }

    /**
     * Programmatically adds a new {@link Deadline} task to the list and saves changes.
     *
     * @param description The description of the deadline.
     * @param byDate The date or time string by which the task must be completed.
     * @throws BertException If the date/time format is invalid.
     */
    private void handleDeadline(String description, String byDate)
            throws BertException {
        LocalDateTime parsedByDate = DateTimeParser.parse(byDate);
        Deadline deadline = new Deadline(description, parsedByDate);
        addTask(deadline);
    }

    /**
     * Programmatically adds a new {@link Event} task to the list and saves changes.
     *
     * @param description The description of the event.
     * @param fromDate The starting date or time of the event.
     * @param toDate The ending date or time of the event.
     * @throws BertException If the date/time format is invalid.
     */
    private void handleEvent(String description, String fromDate, String toDate) throws BertException {
        LocalDateTime parsedFromDate = DateTimeParser.parse(fromDate);
        LocalDateTime parsedToDate = DateTimeParser.parse(toDate);
        Event event = new Event(description, parsedFromDate, parsedToDate);
        addTask(event);
    }

    /**
     * Adds task of any type to tasklist and calls the UI to print a message.
     *
     * @param task
     */
    private void addTask(Task task) {
        taskList.add(task);
        ui.showMsg("Added " + task.getType());
        ui.showTask(taskList.size(), task);
        storage.save(taskList);
    }

    /**
     * Programmatically prints all items in the task list.
     *
     */
    private void handleList() {
        ui.showTodoList(taskList);
    }

    /**
     * Finds and displays tasks matching the specified keyword in their description.
     *
     * @param keyword The keyword to search for.
     */
    private void handleFind(String keyword) {
        TaskList matchingTasks = taskList.find(keyword);
        ui.showFoundTasks(matchingTasks);
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
            ui.showMsg("Already marked " + task.getType());
            ui.showTask(index, task);
        } else {
            taskList.mark(index);
            ui.showMsg("Marked " + task.getType());
            ui.showTask(index, task);
            storage.save(taskList);
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
            ui.showMsg("Already unmarked " + task.getType());
            ui.showTask(index, task);
        } else {
            taskList.unmark(index);
            ui.showMsg("Unmarked " + task.getType());
            ui.showTask(index, task);
            storage.save(taskList);
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
        ui.showMsg("Removed " + task.getType());
        ui.showTask(index, task);
        storage.save(taskList);
    }

    /**
     * Starts the BERT application with default storage settings.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Bert bert = new Bert("data/todo_list_1.txt", System.in, System.out);
        bert.run();
    }
}
