package kpei;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import kpei.storage.StorageConfig;

/**
 * Resolves the launch mode and save-file path from command-line arguments and configuration.
 */
public class LaunchConfiguration {
    private static final String CLI_FLAG = "--cli";
    private static final String USAGE = "Usage: java -jar bert.jar [--cli] <save-file-path>";

    private final StorageConfig storageConfig;

    /**
     * Constructs a launch configuration backed by the specified configuration file.
     *
     * @param configFile The configuration file that stores the last save-file path.
     */
    public LaunchConfiguration(Path configFile) {
        storageConfig = new StorageConfig(configFile);
    }

    /**
     * Returns whether the arguments request CLI mode.
     *
     * @param args Command line arguments.
     * @return Whether CLI mode was requested.
     */
    public boolean isCli(String[] args) {
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
    public String getStorageFilePath(String[] args) throws IOException {
        List<String> saveFilePaths = new ArrayList<>();
        for (String arg : args) {
            if (!CLI_FLAG.equalsIgnoreCase(arg.trim())) {
                saveFilePaths.add(arg);
            }
        }

        if (saveFilePaths.size() > 1) {
            throw new IllegalArgumentException("Only one save file path can be specified.\n" + USAGE);
        }

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
