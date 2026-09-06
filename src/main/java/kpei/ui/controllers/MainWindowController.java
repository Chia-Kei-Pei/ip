package kpei.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import kpei.Bert;

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
        if (cliTerminalController != null) {
            cliTerminalController.setCommandConsumer(this::handleCommand);
        }
    }

    /**
     * Injects the {@link Bert} application instance and refreshes the task list display.
     *
     * @param bert The BERT domain controller instance.
     */
    public void setBert(Bert bert) {
        this.bert = bert;
        refreshTaskList();
    }

    /**
     * Refreshes the task list displayed in the right-hand List View component.
     */
    public void refreshTaskList() {
        if (bert != null && listViewController != null) {
            listViewController.updateTasks(bert.getTaskList(), bert.getStorageFilePath());
        }
    }

    /**
     * Handles command submission from the CLI terminal component.
     *
     * @param input The command entered by the user.
     */
    private void handleCommand(String input) {
        if (bert == null) {
            return;
        }

        boolean isExit = bert.handleUserCommand(input);
        refreshTaskList();

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
