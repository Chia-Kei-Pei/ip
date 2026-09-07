package kpei.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.storage.Storage;
import kpei.ui.Cli;

/**
 * Main window controller coordinating the Banner, CLI Terminal, and List View components.
 */
public class MainWindowController {

    @FXML
    private Node banner;

    @FXML
    private BannerController bannerController;

    @FXML
    private Node cliTerminal;

    @FXML
    private CliTerminalController cliTerminalController;

    @FXML
    private Node listView;

    @FXML
    private ListViewController listViewController;

    private Storage storage;
    private TaskList taskList;
    private Cli cli;

    /**
     * Initializes the controller and binds the terminal command consumer.
     */
    @FXML
    public void initialize() {
        if (cliTerminalController != null) {
            cliTerminalController.setCommandConsumer(this::handleCommand);
        }
    }

    /**
     * Injects the shared storage, task list, and CLI interface instances.
     *
     * @param storage The storage handler instance.
     * @param taskList The task list instance.
     * @param cli The CLI interface instance.
     */
    public void setDependencies(Storage storage, TaskList taskList, Cli cli) {
        this.storage = storage;
        this.taskList = taskList;
        this.cli = cli;
        refreshTaskList();
    }

    /**
     * Injects the {@link Cli} interface instance.
     *
     * @param cli The CLI interface instance.
     */
    public void setCli(Cli cli) {
        this.cli = cli;
        if (cli != null) {
            this.storage = cli.getStorage();
            this.taskList = cli.getTaskList();
        }
        refreshTaskList();
    }

    /**
     * Starts the GUI session by loading tasks from storage, printing greetings, and refreshing the list.
     */
    public void startGui() {
        if (storage != null && taskList != null) {
            try {
                storage.load(taskList);
            } catch (BertException e) {
                if (cli != null) {
                    cli.showWarning(e.getMessage());
                }
            }
        }

        if (cli != null) {
            cli.greeting();
            cli.showLine();
        }
        refreshTaskList();
    }

    /**
     * Refreshes the task list displayed in the right-hand List View component.
     */
    public void refreshTaskList() {
        if (taskList != null && storage != null && listViewController != null) {
            listViewController.updateTasks(taskList, storage.getFilePath());
        }
    }

    /**
     * Handles command submission from the CLI terminal component.
     *
     * @param input The command entered by the user.
     */
    private void handleCommand(String input) {
        if (cli == null) {
            return;
        }

        boolean isExit = cli.executeUserCommand(input);
        refreshTaskList();

        if (isExit) {
            Platform.exit();
        }
    }

    /**
     * Returns the injected {@link Storage} instance.
     *
     * @return The storage instance.
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Returns the injected {@link TaskList} instance.
     *
     * @return The task list instance.
     */
    public TaskList getTaskList() {
        return taskList;
    }

    /**
     * Returns the CLI interface instance.
     *
     * @return The {@link Cli} instance.
     */
    public Cli getCli() {
        return cli;
    }

    /**
     * Returns the nested controller for the CLI terminal.
     *
     * @return The {@link CliTerminalController} instance.
     */
    public CliTerminalController getCliTerminalController() {
        return cliTerminalController;
    }

    /**
     * Returns the nested controller for the banner.
     *
     * @return The {@link BannerController} instance.
     */
    public BannerController getBannerController() {
        return bannerController;
    }

    /**
     * Returns the nested controller for the list view.
     *
     * @return The {@link ListViewController} instance.
     */
    public ListViewController getListViewController() {
        return listViewController;
    }
}
