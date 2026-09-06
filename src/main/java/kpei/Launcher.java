package kpei;

import javafx.application.Application;

/**
 * Launcher class to start BERT application and handle JavaFX classpath initialization.
 */
public class Launcher {

    /**
     * Main entry point delegating to {@link Bert#main(String[])}.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Bert.main(args);
    }

    /**
     * Launches the JavaFX GUI application.
     *
     * @param args Command line arguments passed to JavaFX.
     */
    public static void launchGui(String[] args) {
        Application.launch(Main.class, args);
    }
}
