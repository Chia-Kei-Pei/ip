package kpei;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;

import kpei.datatypes.TaskList;
import kpei.storage.Storage;
import kpei.storage.StorageConfig;
import kpei.ui.Cli;
import kpei.ui.Gui;

/**
 * Entry point that initializes core resources and launches BERT in CLI or GUI mode.
 */
public class Launcher {

    private static final String CLI_FLAG = "--cli";
    private static final Path CONFIG_FILE = Path.of("config.txt");
    private static final String USAGE = "Usage: java -jar bert.jar [--cli] <save-file-path>";

    /**
     * Main method deciding whether to launch the CLI or GUI version of BERT.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        try {
            String storageFilePath = getStorageFilePath(args);
            if (isCli(args)) {
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

    /**
     * Returns whether the arguments request CLI mode.
     *
     * @param args Command line arguments.
     * @return Whether CLI mode was requested.
     */
    private static boolean isCli(String[] args) {
        for (String arg : args) {
            if (CLI_FLAG.equalsIgnoreCase(arg.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the supplied save-file path or the last configured path.
     *
     * @param args Command line arguments.
     * @return A valid save-file path.
     * @throws IOException If the configuration file cannot be read or written.
     * @throws IllegalArgumentException If the arguments or configured path are invalid.
     */
    private static String getStorageFilePath(String[] args) throws IOException {
        List<String> saveFilePaths = new ArrayList<>();
        for (String arg : args) {
            if (!CLI_FLAG.equalsIgnoreCase(arg.trim())) {
                saveFilePaths.add(arg);
            }
        }

        if (saveFilePaths.size() > 1) {
            throw new IllegalArgumentException("Only one save file path can be specified.\n" + USAGE);
        }

        StorageConfig storageConfig = new StorageConfig(CONFIG_FILE);
        if (saveFilePaths.size() == 1) {
            String saveFilePath = saveFilePaths.getFirst();
            Path.of(saveFilePath);
            storageConfig.saveSaveFilePath(saveFilePath);
            return saveFilePath;
        }

        String saveFilePath = storageConfig.loadSaveFilePath()
                .orElseThrow(() -> new IllegalArgumentException(
                        "A save file path is required on first run.\n" + USAGE));
        Path.of(saveFilePath);
        return saveFilePath;
    }
}
