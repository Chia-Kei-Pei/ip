package kpei.ui.controllers;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import kpei.Bert;
import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
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
        cliTerminalController.setCommandConsumer(this::handleCommand);
    }

    /**
     * Injects the shared storage, task list, and CLI interface instances.
     *
     * @param storage The storage handler instance.
     * @param taskList The task list instance.
     * @param cli The CLI interface instance.
     * @param bert The Bert controller instance.
     */
    public void setDependencies(Storage storage, TaskList taskList, Cli cli, Bert bert) {
        this.storage = storage;
        this.taskList = taskList;
        this.cli = cli;
        this.bert = bert;
        refreshTaskList(taskList.getTodos(), storage.getFileName());
    }

    /**
     * Refreshes the task list displayed in the right-hand List View component.
     *
     * @param tasks The list of tasks to display.
     * @param storageFileName The file name of the storage file.
     */
    public void refreshTaskList(ArrayList<Task> tasks, String storageFileName) {
        listViewController.updateTasks(tasks, storageFileName);
    }

    /**
     * Handles command submission from the CLI terminal component.
     *
     * @param input The command entered by the user.
     */
    private void handleCommand(String input) {
        boolean isExit = bert.executeUserCommand(input);

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
