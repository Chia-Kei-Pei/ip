package kpei.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests the save-file path configuration used by {@link StorageConfig}.
 */
class StorageConfigTest {

    @TempDir
    Path tempDir;

    @Test
    void loadSaveFilePath_configFileMissing_emptyOptionalReturned() throws IOException {
        Path configFile = tempDir.resolve("config.txt");
        StorageConfig storageConfig = new StorageConfig(configFile);

        assertTrue(storageConfig.loadSaveFilePath().isEmpty());
    }

    @Test
    void saveAndLoadSaveFilePath_validRelativePath_pathPreserved() throws IOException {
        Path configFile = tempDir.resolve("config.txt");
        StorageConfig storageConfig = new StorageConfig(configFile);
        String expectedSaveFilePath = "saves/my_todo_list.txt";

        storageConfig.saveSaveFilePath(expectedSaveFilePath);

        assertEquals("save_file=saves/my_todo_list.txt" + System.lineSeparator(),
                Files.readString(configFile, StandardCharsets.UTF_8));
        assertEquals(expectedSaveFilePath, storageConfig.loadSaveFilePath().orElseThrow());
    }

    @Test
    void loadSaveFilePath_blankSetting_emptyOptionalReturned() throws IOException {
        Path configFile = tempDir.resolve("config.txt");
        Files.writeString(configFile, "save_file=   " + System.lineSeparator(), StandardCharsets.UTF_8);
        StorageConfig storageConfig = new StorageConfig(configFile);

        assertTrue(storageConfig.loadSaveFilePath().isEmpty());
    }
}
