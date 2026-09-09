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
import kpei.ui.controllers.MainWindowController;

/**
 * Core coordinator and command handler for the BERT assistant.
 * Processes user input commands, mutates application state and storage,
 * and instructs the user interface on what to display.
 */
public class Bert {

    private final Storage storage;
    private final TaskList taskList;
    private final Cli cli;
    private final MainWindowController mainWindowController;
    private final boolean isGuiEnabled;

    private TaskList displayList;

    /**
     * Constructs a {@code Bert} instance with the given storage, task list, and CLI interface.
     *
     * @param storage Storage instance used for task persistence.
     * @param taskList Task list holding the user tasks.
     * @param cli CLI interface used to display messages to the user.
     */
    public Bert(Storage storage, TaskList taskList, Cli cli) {
        this(storage, taskList, cli, null, false);
    }

    /**
     * Constructs a {@code Bert} instance with the given storage, task list, CLI interface,
     * and main window controller.
     *
     * @param storage Storage instance used for task persistence.
     * @param taskList Task list holding the user tasks.
     * @param cli CLI interface used to display messages to the user.
     * @param mainWindowController Controller for the main GUI window.
     */
    public Bert(Storage storage, TaskList taskList, Cli cli, MainWindowController mainWindowController) {
        this(storage, taskList, cli, mainWindowController, true);
    }


    private Bert(Storage storage, TaskList taskList, Cli cli, MainWindowController mainWindowController,
                boolean isGuiEnabled) {
        this.storage = storage;
        this.taskList = taskList;
        this.cli = cli;
        this.mainWindowController = mainWindowController;
        this.isGuiEnabled = isGuiEnabled;

        try {
            this.storage.load(taskList);
        } catch (BertException e) {
            this.cli.showWarning(e.getMessage());
        }

        this.cli.greeting();
        this.cli.showLine();

        displayList = this.taskList;
        
        updateGuiList(displayList, storage.getFileName());
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
        storage.save(taskList);
        cli.print("Added " + task.getType());
        cli.showTask(taskList.size(), task);
        displayList = taskList;
        updateGuiList(displayList, storage.getFileName());
    }

    private void handleList() {
        if (taskList.isEmpty()) {
            cli.print("List is empty.");
        } else {
            displayList = taskList;
            if (!isGuiEnabled) {
                cli.showTodoList(taskList);
            } else {
                cli.print(String.format("Displayed list of size %d.", displayList.size()));
                updateGuiList(displayList, storage.getFileName());
            }
        }
    }

    private void handleFind(String keyword) {
        TaskList matchingTasks = taskList.find(keyword);
        displayList = matchingTasks;
        if (!isGuiEnabled) {
            cli.showFoundTasks(matchingTasks);
        } else {
            cli.print(String.format("Found %d matching tasks.", displayList.size()));
            updateGuiList(displayList, "Search results");
        }
    }

    private void handleMark(int index) throws InvalidIndexException, BertException {
        Task task = displayList.get(index);
        if (task.isMarked()) {
            cli.print("Already marked " + task.getType());
            cli.showTask(index, task);
        } else {
            displayList.mark(index);
            storage.save(taskList); // do NOT save displaylist
            cli.print("Marked " + task.getType());
            cli.showTask(index, task);
            updateGuiList(displayList, storage.getFileName());
        }
    }

    private void handleUnmark(int index) throws InvalidIndexException, BertException {
        Task task = displayList.get(index);
        if (!task.isMarked()) {
            cli.print("Already unmarked " + task.getType());
            cli.showTask(index, task);
        } else {
            displayList.unmark(index);
            storage.save(taskList); // do NOT save displaylist
            cli.print("Unmarked " + task.getType());
            cli.showTask(index, task);
            updateGuiList(displayList, storage.getFileName());
        }
    }

    private void handleDelete(int index) throws InvalidIndexException, BertException {
        Task removedTask = displayList.remove(index);
        storage.save(taskList); // do NOT save displaylist
        cli.print("Removed " + removedTask.getType());
        cli.showTask(index, removedTask);
        updateGuiList(displayList, storage.getFileName());
    }

    private void updateGuiList(TaskList tasks, String listName) {
        if (isGuiEnabled) {
            mainWindowController.refreshTaskList(tasks, listName);
        }
    }
}
