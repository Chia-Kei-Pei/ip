import datatypes.Deadline;
import datatypes.Event;
import datatypes.Todo;
import datatypes.TodoList;
import exceptions.BertException;
import exceptions.InvalidIndexException;
import exceptions.UnknownCommandException;
import parser.CommandParser;
import parser.ParsedCommand;

/**
 * The main application class for BERT task assistant.
 * Handles user interaction loop and programmatic execution of commands.
 */
public class Bert {

    /**
     * Entry point for running the BERT assistant CLI application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        TodoList todoList = new TodoList();

        String banner = """
 ____     ___  ____  ______
|    \\   /  _]|    \\|      |
|  o  ) /  [_ |  D  )      |
|     ||    _]|    /|_|  |_|
|  O  ||   [_ |    \\  |  | 
|     ||     ||  .  \\ |  | 
|_____||_____||__|\\_| |__| 
""";
        IO.println("\n------------------------------------------------------------");
        IO.println(banner);
        IO.println("I am  B E R T.");
        IO.println("What do you need?");

        while (true) {
            IO.println("\n------------------------------------------------------------");
            String userPrompt = IO.readln("> ");
            IO.println("\n------------------------------------------------------------");

            try {
                ParsedCommand command = CommandParser.parse(userPrompt);

                if (command.isExitCommand()) {
                    IO.println("Goodbye.");
                    return;
                }

                executeCommand(command, todoList);
            } catch (BertException | IllegalArgumentException e) {
                IO.println(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                IO.println(e);
            }
        }
    }

    /**
     * Executes a parsed command by extracting its arguments and dispatching to the appropriate handler method.
     *
     * @param cmd The parsed command containing command type, arguments, and flags.
     * @param todoList The task list to operate on.
     * @throws BertException If an application-level error occurs during execution.
     * @throws IllegalArgumentException If an argument format is invalid.
     */
    public static void executeCommand(ParsedCommand cmd, TodoList todoList)
            throws BertException, IllegalArgumentException {
        switch (cmd.getCommandType()) {
        case "todo" -> handleTodo(cmd.getArgument(), todoList);
        case "deadline" -> handleDeadline(cmd.getArgument(), cmd.getFlag("by"), todoList);
        case "event" -> handleEvent(cmd.getArgument(), cmd.getFlag("from"), cmd.getFlag("to"), todoList);
        case "list" -> handleList(todoList);
        case "mark" -> handleMark(cmd.getArgumentAsInt(), todoList);
        case "unmark" -> handleUnmark(cmd.getArgumentAsInt(), todoList);
        case "delete", "remove" -> handleDelete(cmd.getArgumentAsInt(), todoList);
        default -> throw new UnknownCommandException(cmd.getCommandType());
        }
    }

    /**
     * Programmatically adds a new {@link Todo} todo to the list.
     *
     * @param description The description of the todo task.
     * @param todoList The task list to add the todo to.
     */
    public static void handleTodo(String description, TodoList todoList) {
        Todo todo = new Todo(description);
        todoList.add(todo);
    }

    /**
     * Programmatically adds a new {@link Deadline} todo to the list.
     *
     * @param description The description of the deadline.
     * @param byDate The date or time string by which the task must be completed.
     * @param todoList The task list to add the deadline to.
     */
    public static void handleDeadline(String description, String byDate, TodoList todoList) {
        Deadline deadline = new Deadline(description, byDate);
        todoList.add(deadline);
    }

    /**
     * Programmatically adds a new {@link Event} todo to the list.
     *
     * @param description The description of the event.
     * @param fromDate The starting date or time of the event.
     * @param toDate The ending date or time of the event.
     * @param todoList The task list to add the event to.
     */
    public static void handleEvent(String description, String fromDate, String toDate, TodoList todoList) {
        Event event = new Event(description, fromDate, toDate);
        todoList.add(event);
    }

    /**
     * Programmatically prints all todos in the task list.
     *
     * @param todoList The task list to display.
     */
    public static void handleList(TodoList todoList) {
        todoList.printList();
    }

    /**
     * Programmatically marks a task at the given 1-based index as completed.
     *
     * @param index The 1-based index of the task.
     * @param todoList The task list containing the task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public static void handleMark(int index, TodoList todoList) throws InvalidIndexException {
        todoList.mark(index);
    }

    /**
     * Programmatically unmarks a task at the given 1-based index.
     *
     * @param index The 1-based index of the task.
     * @param todoList The task list containing the task.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public static void handleUnmark(int index, TodoList todoList) throws InvalidIndexException {
        todoList.unmark(index);
    }

    /**
     * Programmatically removes a task at the given 1-based index from the list.
     *
     * @param index The 1-based index of the task.
     * @param todoList The task list to remove the task from.
     * @throws InvalidIndexException If the index is outside the valid range.
     */
    public static void handleDelete(int index, TodoList todoList) throws InvalidIndexException {
        todoList.remove(index);
    }
}
