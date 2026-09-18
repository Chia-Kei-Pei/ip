package kpei;

import java.io.IOException;
import java.nio.file.Path;

import javafx.application.Application;

import kpei.datatypes.TaskList;
import kpei.storage.Storage;
import kpei.ui.Cli;
import kpei.ui.Gui;

/**
 * Entry point that initializes core resources and launches BERT in CLI or GUI mode.
 */
public class Launcher {

    private static final Path CONFIG_FILE = Path.of("config.txt");

    /**
     * Main method deciding whether to launch the CLI or GUI version of BERT.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            LaunchConfiguration launchConfiguration = new LaunchConfiguration(CONFIG_FILE);
            String storageFilePath = launchConfiguration.getStorageFilePath(args);
            if (launchConfiguration.isCli(args)) {
                Storage storage = new Storage(storageFilePath);
                TaskList taskList = new TaskList(storage.getFileName());
                Cli cli = new Cli(System.in, System.out);
                CommandCenter commandCenter = new CommandCenter(storage, taskList, cli);
                cli.runCliOnly(commandCenter);
            } else {
                Gui.initDependencies(storageFilePath);
                Application.launch(Gui.class, args);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
