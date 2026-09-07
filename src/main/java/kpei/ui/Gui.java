package kpei.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import kpei.Bert;
import kpei.datatypes.TaskList;
import kpei.storage.Storage;
import kpei.ui.controllers.MainWindowController;

/**
 * JavaFX Application class bootstrapping the GUI for BERT Assistant.
 */
public class Gui extends Application {

    private static final String MAIN_WINDOW_FXML = "/kpei/views/MainWindow.fxml";
    private static final String DEFAULT_DATA_PATH = "data/todo_list_1.txt";
    private static final String APPLICATION_TITLE = "BERT Assistant";
    private static final double MIN_WIDTH = 850;
    private static final double MIN_HEIGHT = 580;

    private static Storage storage;
    private static TaskList taskList;

    /**
     * Sets the shared storage and task list dependencies initialized by Launcher.
     *
     * @param storageInstance The initialized Storage instance.
     * @param taskListInstance The initialized TaskList instance.
     */
    public static void initDependencies(Storage storageInstance, TaskList taskListInstance) {
        storage = storageInstance;
        taskList = taskListInstance;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_WINDOW_FXML));
        Parent root = fxmlLoader.load();
        MainWindowController mainWindowController = fxmlLoader.getController();

        Storage effectiveStorage = storage != null ? storage : new Storage(DEFAULT_DATA_PATH);
        TaskList effectiveTaskList = taskList != null ? taskList : new TaskList();

        Cli cli = new Cli(msg -> mainWindowController.getCliTerminalController().appendOutput(msg));
        Bert bert = new Bert(effectiveStorage, effectiveTaskList, cli, mainWindowController);
        cli.setBert(bert);

        mainWindowController.setDependencies(effectiveStorage, effectiveTaskList, cli, bert);

        Scene scene = new Scene(root);
        stage.setTitle(APPLICATION_TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
