package kpei;

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
 * Core coordinator and command handler for the BERT assistant.
 * Processes user input commands, mutates application state and storage,
 * and instructs the user interface on what to display.
 */
public class Bert {

    private final Storage storage;
    private final TaskList taskList;
    private final Cli cli;

    /**
     * Constructs a {@code Bert} instance with the given storage, task list, and CLI interface.
     *
     * @param storage Storage instance used for task persistence.
     * @param taskList Task list holding the user tasks.
     * @param cli CLI interface used to display messages to the user.
     */
    public Bert(Storage storage, TaskList taskList, Cli cli) {
        this.storage = storage;
        this.taskList = taskList;
        this.cli = cli;
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
     * Dispatches a parsed command to the appropriate handler method.
     *
     * @param userPrompt The raw command string entered by the user.
     * @return {@code true} if an exit command was executed, {@code false} otherwise.
     */
    public boolean executeUserCommand(String userPrompt) {
        try {
            cli.showLine();
            ParsedCommand cmd = CommandParser.parse(userPrompt);

            switch (cmd.getCommandType()) {
                case "todo" -> handleAdd(TaskParser.parseTodo(cmd.getArgument()));
                case "deadline" -> handleAdd(TaskParser.parseDeadline(cmd.getArgument(), cmd.getFlag("by")));
                case "event" -> handleAdd(TaskParser.parseEvent(cmd.getArgument(),
                        cmd.getFlag("from"), cmd.getFlag("to")));
                case "list" -> handleList();
                case "find" -> handleFind(cmd.getArgument());
                case "mark" -> handleMark(cmd.getArgumentAsInt());
                case "unmark" -> handleUnmark(cmd.getArgumentAsInt());
                case "delete" -> handleDelete(cmd.getArgumentAsInt());
                case "exit" -> {
                    cli.farewell();
                    return true;
                }
                default -> throw new UnknownCommandException(cmd.getCommandType());
            }
        } catch (BertException | IllegalArgumentException | IndexOutOfBoundsException e) {
            cli.showError(e.getMessage());
        } finally {
            cli.showLine();
        }
        return false;
    }

    private void handleAdd(Task task) throws BertException {
        taskList.add(task);
        saveStorage();
        cli.showMsg("Added " + task.getType());
        cli.showTask(taskList.size(), task);
    }

    private void handleList() {
        if (taskList.isEmpty()) {
            cli.showMsg("List is empty.");
        } else {
            cli.showTodoList(taskList);
        }
    }

    private void handleFind(String keyword) {
        TaskList matchingTasks = taskList.find(keyword);
        cli.showFoundTasks(matchingTasks);
    }

    private void handleMark(int index) throws InvalidIndexException, BertException {
        Task task = taskList.get(index);
        if (task.isMarked()) {
            cli.showMsg("Already marked " + task.getType());
            cli.showTask(index, task);
        } else {
            taskList.mark(index);
            saveStorage();
            cli.showMsg("Marked " + task.getType());
            cli.showTask(index, task);
        }
    }

    private void handleUnmark(int index) throws InvalidIndexException, BertException {
        Task task = taskList.get(index);
        if (!task.isMarked()) {
            cli.showMsg("Already unmarked " + task.getType());
            cli.showTask(index, task);
        } else {
            taskList.unmark(index);
            saveStorage();
            cli.showMsg("Unmarked " + task.getType());
            cli.showTask(index, task);
        }
    }

    private void handleDelete(int index) throws InvalidIndexException, BertException {
        Task removedTask = taskList.remove(index);
        saveStorage();
        cli.showMsg("Removed " + removedTask.getType());
        cli.showTask(index, removedTask);
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
     * Returns the storage instance used by this instance.
     *
     * @return The storage instance.
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Returns the CLI interface used by this instance.
     *
     * @return The {@link Cli} instance.
     */
    public Cli getCli() {
        return cli;
    }
}
