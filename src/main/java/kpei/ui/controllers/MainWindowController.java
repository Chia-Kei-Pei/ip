package kpei.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import kpei.Bert;
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
    private Bert bert;

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
    public void setDependencies(Storage storage, TaskList taskList, Cli cli, Bert bert) {
        this.storage = storage;
        this.taskList = taskList;
        this.cli = cli;
        this.bert = bert;
        refreshTaskList();
    }

    /**
     * Refreshes the task list displayed in the right-hand List View component.
     */
    public void refreshTaskList() {
        listViewController.updateTasks(taskList, storage.getFilePath());
    }

    /**
     * Handles command submission from the CLI terminal component.
     *
     * @param input The command entered by the user.
     */
    private void handleCommand(String input) {
        boolean isExit = bert.executeUserCommand(input);
        refreshTaskList(); // Todo: should be moved into Bert handler methods

        if (isExit) {
            Platform.exit();
        }
    }

    /**
     * Returns the nested controller for the CLI terminal.
     *
     * @return The {@link CliTerminalController} instance.
     */
    public CliTerminalController getCliTerminalController() {
        return cliTerminalController;
    }
}
