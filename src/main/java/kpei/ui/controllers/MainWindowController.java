package kpei.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import kpei.Bert;
import kpei.datatypes.TaskList;

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

    private Bert bert;

    /**
     * Initializes the controller and binds the terminal command consumer.
     */
    @FXML
    public void initialize() {
        cliTerminalController.setCommandConsumer(this::handleCommand);
    }

    /**
     * Injects the bert instance.
     *
     * @param bert The Bert controller instance.
     */
    public void setDependencies(Bert bert) {
        this.bert = bert;
    }

    /**
     * Refreshes the task list displayed in the right-hand List View component.
     *
     * @param taskList The TaskList of tasks to display.
     */
    public void refreshTaskList(TaskList taskList) {
        listViewController.updateTasks(taskList);
    }

    /**
     * Handles command submission to bert.
     *
     * @param input The command entered by the user.
     */
    private void handleCommand(String input) {
        boolean isExit = bert.executeCommand(input);

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
