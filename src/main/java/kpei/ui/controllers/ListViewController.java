package kpei.ui.controllers;

import java.io.IOException;
import java.nio.file.Path;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import kpei.datatypes.Task;
import kpei.datatypes.TaskList;

/**
 * Controller for the List View component on the right side of the application.
 */
public class ListViewController {

    @FXML
    private Label listTitle;

    @FXML
    private ListView<Task> taskListView;

    /**
     * Initializes the list view by setting a custom cell factory backed by ListItem.fxml.
     */
    @FXML
    public void initialize() {
        taskListView.setCellFactory(param -> new ListCell<Task>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/kpei/views/ListItem.fxml"));
                        Node node = loader.load();
                        ListItemController controller = loader.getController();
                        controller.setTask(getIndex() + 1, task);
                        setGraphic(node);
                        setText(null);
                    } catch (IOException e) {
                        setText(task.toString());
                    }
                }
            }
        });
    }

    /**
     * Updates the displayed tasks and sets the title to the file name of the storage path.
     *
     * @param taskList Current task list to display.
     * @param storageFilePath File path of the storage data file.
     */
    public void updateTasks(TaskList taskList, String storageFilePath) {
        if (storageFilePath != null && !storageFilePath.isBlank()) {
            Path path = Path.of(storageFilePath);
            listTitle.setText(path.getFileName().toString());
        }

        ObservableList<Task> items = FXCollections.observableArrayList();
        if (taskList != null) {
            items.addAll(taskList.getTodos());
        }
        taskListView.setItems(items);
    }
}
