package kpei;

import kpei.commands.user.deadlineCommand;
import kpei.commands.user.eventCommand;
import kpei.commands.user.todoCommand;
import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.exceptions.InvalidIndexException;
import kpei.exceptions.UnknownCommandException;
import kpei.commands.parser.CommandParser;
import kpei.commands.parser.ParsedCommand;
import kpei.storage.Storage;
import kpei.ui.Cli;
import kpei.ui.controllers.MainWindowController;

import java.util.List;

/**
 * Core coordinator and Command handler for the BERT assistant.
 * Processes user input commands, mutates application state and storage,
 * and instructs the user interface on what to display.
 */
public class CommandCenter {

    private final Storage storage;
    private final TaskList taskList;
    private final Cli cli;
    private final MainWindowController mainWindowController;
    private final boolean isGuiEnabled;

    private TaskList displayList;

    private todoCommand todoCommand;
    private deadlineCommand deadlineCommand;
    private eventCommand eventCommand;

    /**
     * Constructs a {@code CommandCenter} instance with the given storage, task list, and CLI interface.
     *
     * @param storage Storage instance used for task persistence.
     * @param taskList Task list holding the user tasks.
     * @param cli CLI interface used to display messages to the user.
     */
    public CommandCenter(Storage storage, TaskList taskList, Cli cli) {
        this(storage, taskList, cli, null, false);
    }

    /**
     * Constructs a {@code CommandCenter} instance with the given storage, task list, CLI interface,
     * and main window controller.
     *
     * @param storage Storage instance used for task persistence.
     * @param taskList Task list holding the user tasks.
     * @param cli CLI interface used to display messages to the user.
     * @param mainWindowController Controller for the main GUI window.
     */
    public CommandCenter(Storage storage, TaskList taskList, Cli cli, MainWindowController mainWindowController) {
        this(storage, taskList, cli, mainWindowController, true);
    }


    private CommandCenter(Storage storage, TaskList taskList, Cli cli, MainWindowController mainWindowController,
                          boolean isGuiEnabled) {
        this.storage = storage;
        this.taskList = taskList;
        this.cli = cli;
        this.mainWindowController = mainWindowController;
        this.isGuiEnabled = isGuiEnabled;

        todoCommand = new todoCommand();
        deadlineCommand = new deadlineCommand();
        eventCommand = new eventCommand();

        try {
            this.storage.load(taskList);
        } catch (BertException e) {
            this.cli.warning(e.getMessage());
        }

        this.cli.greeting();
        this.cli.horizontalLine();

        displayList = this.taskList;

        displayListGui();
    }

    /**
     * Dispatches a parsed Command to the appropriate handler method.
     *
     * @param userPrompt The raw Command string entered by the user.
     * @return {@code true} if an exit Command was executed, {@code false} otherwise.
     */
    public boolean executeCommand(String userPrompt) {
        try {
            CommandParser commandParser = new CommandParser();

            cli.horizontalLine();

//            ParsedCommand cmd = CommandParser.parse(userPrompt);
            List<String> tokens = commandParser.tokenize(userPrompt);
            ParsedCommand cmd = commandParser.parse(tokens);

            if (todoCommand.isMatch(cmd)) {
                handleAdd(todoCommand.execute(cmd));
            } else if (deadlineCommand.isMatch(cmd)) {
                handleAdd(deadlineCommand.execute(cmd));
            } else if (eventCommand.isMatch(cmd)) {
                handleAdd(eventCommand.execute(cmd));
//            } else if (cmd.getCommandType().equals("find")) {
//                handleFind(cmd.getArguments());
//            } else if (cmd.getCommandType().equals("mark")) {
//                handleMark(cmd.getArguments());
//            } else if (cmd.getCommandType().equals("unmark")) {
//                handleUnmark(cmd.getArguments());
//            } else if (cmd.getCommandType().equals("delete")) {
//                handleDelete(cmd.getArguments());
            } else if (cmd.getCommandType().equals("list")) {
                handleList();
            } else if (cmd.getCommandType().equals("exit")) {
                cli.farewell();
                return true;
            } else {
                throw new UnknownCommandException(cmd.getCommandType());
            }
        } catch (BertException e) { // IllegalArgumentException | IndexOutOfBoundsException e
            cli.error(e.getMessage());
        } finally {
            cli.horizontalLine();
        }
        return false;
    }

    private void handleAdd(Task task) throws BertException {
        taskList.add(task);
        saveList();
        cli.print("Added " + task.getType());
        cli.printTask(taskList.size(), task);
        displayList = taskList;
        displayListGui();
    }

    private void handleList() throws InvalidIndexException {
        if (taskList.isEmpty()) {
            cli.print("List is empty.");
        } else {
            displayList = taskList;
            cli.print(String.format("Displaying list of size %d.", displayList.size()));
            if (!isGuiEnabled) {
                cli.printList(taskList);
            } else {
                displayListGui();
            }
        }
    }

    private void handleFind(String keyword) throws InvalidIndexException {
        TaskList matchingTasks = taskList.find(keyword);
        displayList = matchingTasks;
        cli.print(String.format("Found %d matching tasks.", displayList.size()));
        if (!isGuiEnabled) {
            cli.printList(displayList);
        } else {
            displayListGui();
        }
    }

    private void handleMark(int index) throws InvalidIndexException, BertException {
        Task task = displayList.get(index);
        if (task.isMarked()) {
            cli.print("Already marked " + task.getType());
            cli.printTask(index, task);
        } else {
            displayList.mark(index);
            saveList();
            cli.print("Marked " + task.getType());
            cli.printTask(index, task);
            displayListGui();
        }
    }

    private void handleUnmark(int index) throws InvalidIndexException, BertException {
        Task task = displayList.get(index);
        if (!task.isMarked()) {
            cli.print("Already unmarked " + task.getType());
            cli.printTask(index, task);
        } else {
            displayList.unmark(index);
            saveList();
            cli.print("Unmarked " + task.getType());
            cli.printTask(index, task);
            displayListGui();
        }
    }

    private void handleDelete(int index) throws InvalidIndexException, BertException {
        Task removedTask = displayList.remove(index);
        saveList();
        cli.print("Removed " + removedTask.getType());
        cli.printTask(index, removedTask);
        displayListGui();
    }

    private void saveList() throws BertException {
        storage.save(taskList); // do NOT save displaylist
    }

    private void displayListGui() {
        if (isGuiEnabled) {
            mainWindowController.refreshTaskList(displayList);
        }
    }
}
