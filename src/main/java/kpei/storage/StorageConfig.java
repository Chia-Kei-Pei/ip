package kpei.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * Stores the path of the most recently opened save file.
 */
public class StorageConfig {
    private static final String SAVE_FILE_KEY = "save_file";

    private final Path configFile;

    /**
     * Constructs a storage configuration backed by the specified text file.
     *
     * @param configFile The text file that stores the save-file path.
     */
    public StorageConfig(Path configFile) {
        this.configFile = configFile;
    }

    /**
     * Returns the configured save-file path, if one is available.
     *
     * @return The configured save-file path, or an empty optional when no path is configured.
     * @throws IOException If the configuration file cannot be read.
     */
    public Optional<String> loadSaveFilePath() throws IOException {
        if (!Files.exists(configFile)) {
            return Optional.empty();
        }

        for (String line : Files.readAllLines(configFile, StandardCharsets.UTF_8)) {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith(SAVE_FILE_KEY + "=")) {
                continue;
            }

            String saveFilePath = trimmedLine.substring((SAVE_FILE_KEY + "=").length()).trim();
            if (!saveFilePath.isEmpty()) {
                return Optional.of(saveFilePath);
            }
        }

        return Optional.empty();
    }

    /**
     * Saves the specified save-file path to the configuration file.
     *
     * @param saveFilePath The path to save.
     * @throws IOException If the configuration file cannot be written.
     */
    public void saveSaveFilePath(String saveFilePath) throws IOException {
        Files.createDirectories(configFile.toAbsolutePath().getParent());
        String configContents = SAVE_FILE_KEY + "=" + saveFilePath + System.lineSeparator();
        Files.writeString(configFile, configContents, StandardCharsets.UTF_8);
    }
}
