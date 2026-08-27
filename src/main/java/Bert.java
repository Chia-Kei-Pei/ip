import datatypes.Deadline;
import datatypes.Event;
import datatypes.Todo;
import datatypes.TodoList;
import exceptions.BertException;
import exceptions.InvalidIndexException;
import exceptions.UnknownCommandException;
import parser.CommandParser;
import parser.DateTimeParser;
import parser.ParsedCommand;
import storage.Storage;
import ui.Ui;

import java.time.LocalDateTime;

/**
 * The main application class for BERT task assistant.
 * Handles user interaction loop, task persistence, and programmatic execution of commands.
 */
public class Bert {

    private Storage storage;
    private TodoList todoList;
    private Ui ui;

    public Bert(String todoListFilePath) {
        storage = new Storage(todoListFilePath);
        todoList = new TodoList();
        ui = new Ui();
    }

    /**
     * Entry point for running the BERT assistant CLI application.
     * Initializes storage, loads saved tasks, and starts the command loop.
     *
     * @param args Command line arguments (not used).
     */
    public void run() {
        storage.load(todoList);

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

                executeCommand(command, todoList, storage);
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
     * @param todoList The task list to operate on.
     * @param storage The storage handler used to persist modifications.
     * @throws BertException If an application-level error occurs during execution.
     * @throws IllegalArgumentException If an argument format is invalid.
     */
    private void executeCommand(ParsedCommand cmd, TodoList todoList, Storage storage)
            throws BertException, IllegalArgumentException {
        switch (cmd.getCommandType()) {
            case "todo" -> handleTodo(cmd.getArgument(), todoList, storage);
            case "deadline" -> handleDeadline(cmd.getArgument(), cmd.getFlag("by"), todoList, storage);
            case "event" -> handleEvent(cmd.getArgument(), cmd.getFlag("from"), cmd.getFlag("to"), todoList, storage);
            case "list" -> handleList(todoList);
            case "mark" -> handleMark(cmd.getArgumentAsInt(), todoList, storage);
            case "unmark" -> handleUnmark(cmd.getArgumentAsInt(), todoList, storage);
            case "delete", "remove" -> handleDelete(cmd.getArgumentAsInt(), todoList, storage);
            default -> throw new UnknownCommandException(cmd.getCommandType());
        }
    }

    /**
     * Programmatically adds a new {@link Todo} task to the list and saves changes.
     *
     * @param description The description of the todo task.
     * @param todoList The task list to add the todo to.
     * @param storage The storage handler to persist changes.
     */
    private void handleTodo(String description, TodoList todoList, Storage storage) {
        Todo todo = new Todo(description);
        todoList.add(todo);
        storage.save(todoList);
    }

    /**
     * Programmatically adds a new {@link Deadline} task to the list and saves changes.
     *
     * @param description The description of the deadline.
     * @param byDate The date or time string by which the task must be completed.
     * @param todoList The task list to add the deadline to.
     * @param storage The storage handler to persist changes.
     * @throws BertException If the date/time format is invalid.
     */
    private void handleDeadline(String description, String byDate, TodoList todoList, Storage storage)
            throws BertException {
        LocalDateTime parsedByDate = DateTimeParser.parse(byDate);
        Deadline deadline = new Deadline(description, parsedByDate);
        todoList.add(deadline);
        storage.save(todoList);
    }

    /**
     * Programmatically adds a new {@link Event} task to the list and saves changes.
     *
     * @param description The description of the event.
     * @param fromDate The starting date or time of the event.
     * @param toDate The ending date or time of the event.
     * @param todoList The task list to add the event to.
     * @param storage The storage handler to persist changes.
     * @throws BertException If the date/time format is invalid.
     */
    private void handleEvent(String description, String fromDate, String toDate, TodoList todoList,
            Storage storage) throws BertException {
        LocalDateTime parsedFromDate = DateTimeParser.parse(fromDate);
        LocalDateTime parsedToDate = DateTimeParser.parse(toDate);
        Event event = new Event(description, parsedFromDate, parsedToDate);
        todoList.add(event);
        storage.save(todoList);
    }

    /**
     * Programmatically prints all items in the task list.
     *
     * @param todoList The task list to display.
     */
    private void handleList(TodoList todoList) {
        todoList.printList();
    }

    /**
     * Programmatically marks a task at the given 1-based index as completed and saves changes.
     *
     * @param index The 1-based index of the task.
     * @param todoList The task list containing the task.
     * @param storage The storage handler to persist changes.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleMark(int index, TodoList todoList, Storage storage) throws InvalidIndexException {
        todoList.mark(index);
        storage.save(todoList);
    }

    /**
     * Programmatically unmarks a task at the given 1-based index and saves changes.
     *
     * @param index The 1-based index of the task.
     * @param todoList The task list containing the task.
     * @param storage The storage handler to persist changes.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleUnmark(int index, TodoList todoList, Storage storage) throws InvalidIndexException {
        todoList.unmark(index);
        storage.save(todoList);
    }

    /**
     * Programmatically removes a task at the given 1-based index from the list and saves changes.
     *
     * @param index The 1-based index of the task.
     * @param todoList The task list to remove the task from.
     * @param storage The storage handler to persist changes.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    private void handleDelete(int index, TodoList todoList, Storage storage) throws InvalidIndexException {
        todoList.remove(index);
        storage.save(todoList);
    }

    public static void main(String[] args) {
        Bert bert = new Bert("data/todo_list.txt");
        bert.run();
    }
}
