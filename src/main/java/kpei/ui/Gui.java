package kpei.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_WINDOW_FXML));
        Parent root = fxmlLoader.load();
        MainWindowController mainWindowController = fxmlLoader.getController();

        OutputStream terminalOutputStream = new OutputStream() {
            private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            @Override
            public void write(int b) {
                buffer.write(b);
                if (b == '\n') {
                    flush();
                }
            }

            @Override
            public void write(byte[] b, int off, int len) {
                buffer.write(b, off, len);
                for (int i = off; i < off + len; i++) {
                    if (b[i] == '\n') {
                        flush();
                        break;
                    }
                }
            }

            @Override
            public void flush() {
                if (buffer.size() > 0) {
                    String text = buffer.toString(StandardCharsets.UTF_8);
                    buffer.reset();
                    mainWindowController.getCliTerminalController().appendOutput(text);
                }
            }
        };

        Cli cli = new Cli(DEFAULT_DATA_PATH, terminalOutputStream, true);

        mainWindowController.setCli(cli);
        mainWindowController.startGui();

        Scene scene = new Scene(root);
        stage.setTitle(APPLICATION_TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
