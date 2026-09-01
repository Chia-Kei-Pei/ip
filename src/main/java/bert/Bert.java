package bert;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

import bert.datatypes.*;
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

                executeCommand(command, taskList, storage);
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
     * @param taskList The task list to operate on.
     * @param storage The storage handler used to persist modifications.
     * @throws BertException If an application-level error occurs during execution.
     * @throws IllegalArgumentException If an argument format is invalid.
     */
    private void executeCommand(ParsedCommand cmd, TaskList taskList, Storage storage)
            throws BertException, IllegalArgumentException {
        switch (cmd.getCommandType()) {
            case "todo" -> handleTodo(cmd.getArgument(), taskList, storage);
            case "deadline" -> handleDeadline(cmd.getArgument(), cmd.getFlag("by"), taskList, storage);
            case "event" -> handleEvent(cmd.getArgument(), cmd.getFlag("from"), cmd.getFlag("to"), taskList, storage);
            case "list" -> handleList(taskList);
            case "find" -> handleFind(cmd.getArgument(), taskList);
            case "mark" -> handleMark(cmd.getArgumentAsInt(), taskList, storage);
            case "unmark" -> handleUnmark(cmd.getArgumentAsInt(), taskList, storage);
            case "delete", "remove" -> handleDelete(cmd.getArgumentAsInt(), taskList, storage);
            default -> throw new UnknownCommandException(cmd.getCommandType());
        }
    }

    /**
     * Programmatically adds a new {@link Task} task to the list and saves changes.
     *
     * @param description The description of the todo task.
     * @param taskList The task list to add the todo to.
     * @param storage The storage handler to persist changes.
     */
    private void handleTodo(String description, TaskList taskList, Storage storage) {
        Task todo = new Task(description);
        taskList.add(todo);
        ui.showAdded(todo);
        storage.save(taskList);
    }

    /**
     * Programmatically adds a new {@link Deadline} task to the list and saves changes.
     *
     * @param description The description of the deadline.
     * @param byDate The date or time string by which the task must be completed.
     * @param taskList The task list to add the deadline to.
     * @param storage The storage handler to persist changes.
     * @throws BertException If the date/time format is invalid.
     */
    private void handleDeadline(String description, String byDate, TaskList taskList, Storage storage)
            throws BertException {
        LocalDateTime parsedByDate = DateTimeParser.parse(byDate);
        Deadline deadline = new Deadline(description, parsedByDate);
        taskList.add(deadline);
        ui.showAdded(deadline);
        storage.save(taskList);
    }

    /**
     * Programmatically adds a new {@link Event} task to the list and saves changes.
     *
     * @param description The description of the event.
     * @param fromDate The starting date or time of the event.
     * @param toDate The ending date or time of the event.
     * @param taskList The task list to add the event to.
     * @param storage The storage handler to persist changes.
     * @throws BertException If the date/time format is invalid.
     */
    private void handleEvent(String description, String fromDate, String toDate, TaskList taskList,
            Storage storage) throws BertException {
        LocalDateTime parsedFromDate = DateTimeParser.parse(fromDate);
        LocalDateTime parsedToDate = DateTimeParser.parse(toDate);
        Event event = new Event(description, parsedFromDate, parsedToDate);
        taskList.add(event);
        ui.showAdded(event);
        storage.save(taskList);
    }

    /**
     * Programmatically prints all items in the task list.
     *
     * @param taskList The task list to display.
     */
    private void handleList(TaskList taskList) {
        ui.showTodoList(taskList);
    }

    /**
     * Finds and displays tasks matching the specified keyword in their description.
     *
     * @param keyword The keyword to search for.
     * @param taskList The task list to search within.
     */
    private void handleFind(String keyword, TaskList taskList) {
        TaskList matchingTasks = taskList.find(keyword);
        ui.showFoundTasks(matchingTasks);
    }

    /**
     * Programmatically marks a task at the given 1-based index as completed and saves changes.
     *
     * @param index The 1-based index of the task.
     * @param taskList The task list containing the task.
     * @param storage The storage handler to persist changes.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleMark(int index, TaskList taskList, Storage storage) throws InvalidIndexException {
        Task todo = taskList.get(index);
        if (todo.isMarked()) {
            ui.showAlreadyMarked(index, todo);
        } else {
            taskList.mark(index);
            ui.showMarked(index, todo);
            storage.save(taskList);
        }
    }

    /**
     * Programmatically unmarks a task at the given 1-based index and saves changes.
     *
     * @param index The 1-based index of the task.
     * @param taskList The task list containing the task.
     * @param storage The storage handler to persist changes.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleUnmark(int index, TaskList taskList, Storage storage) throws InvalidIndexException {
        Task todo = taskList.get(index);
        if (!todo.isMarked()) {
            ui.showAlreadyUnmarked(index, todo);
        } else {
            taskList.unmark(index);
            ui.showUnmarked(index, todo);
            storage.save(taskList);
        }
    }

    /**
     * Programmatically removes a task at the given 1-based index from the list and saves changes.
     *
     * @param index The 1-based index of the task.
     * @param taskList The task list to remove the task from.
     * @param storage The storage handler to persist changes.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleDelete(int index, TaskList taskList, Storage storage) throws InvalidIndexException {
        Task removed = taskList.remove(index);
        ui.showRemoved(removed);
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
