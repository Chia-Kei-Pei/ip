package kpei.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.exceptions.BertException;
import kpei.parser.TaskParser;

/**
 * Handles persistent storage of {@link TaskList} tasks to and from a local file.
 * Uses a human-readable pipe-delimited plain text format.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a {@code Storage} handler with a custom file path.
     *
     * @param filePath Relative or absolute path to the data storage file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the save file into the provided {@link TaskList}.
     * If the file does not exist, no action is taken and the list remains as-is.
     * Corrupted or unrecognized lines are safely ignored.
     *
     * @param taskList The list to populate with loaded tasks.
     * @throws BertException If an error occurs while reading or parsing the data file.
     */
    public void load(TaskList taskList) throws BertException {
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.split("\\s*\\|\\s*");
                if (parts.length < 3) {
                    continue;
                }

                String type = parts[0].trim();
                boolean isMarked = Boolean.parseBoolean(parts[1].trim()) || parts[1].trim().equals("1");
                String description = parts[2].trim();

                switch (type) {
                    case "todo" -> taskList.add(TaskParser.parseTodo(isMarked, description));
                    case "deadline" -> {
                        if (parts.length >= 4) {
                            try {
                                taskList.add(TaskParser.parseDeadline(isMarked, description, parts[3].trim()));
                            } catch (BertException e) {
                                throw new BertException("Warning: Skipping task with invalid deadline in "
                                        + filePath + ": " + line, e);
                            }
                        }
                    }
                    case "event" -> {
                        if (parts.length >= 5) {
                            try {
                                taskList.add(TaskParser.parseEvent(isMarked, description,
                                        parts[3].trim(), parts[4].trim()));
                            } catch (BertException e) {
                                throw new BertException("Warning: Skipping task with invalid event dates in "
                                        + filePath + ": " + line, e);
                            }
                        }
                    }
                    default -> {
                        // Ignore unrecognized task types
                    }
                }
            }
        } catch (IOException e) {
            throw new BertException("Warning: Unable to load data from " + filePath + " (" + e.getMessage() + ")", e);
        }
    }

    /**
     * Saves all tasks from the specified {@link TaskList} into the storage file.
     * Creates any missing parent directories automatically.
     *
     * @param taskList The list containing tasks to save.
     * @throws BertException If an I/O error occurs while saving data.
     */
    public void save(TaskList taskList) throws BertException {
        Path path = Path.of(filePath);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            List<String> lines = new ArrayList<>();
            for (Task todo : taskList.getTodos()) {
                lines.add(todo.toFileFormat());
            }

            Files.write(path, lines);
        } catch (IOException e) {
            throw new BertException("Warning: Unable to save data to " + filePath + " (" + e.getMessage() + ")", e);
        }
    }

    /**
     * Returns the file path associated with this storage handler.
     *
     * @return The storage file path.
     */
    public String getFilePath() {
        return filePath;
    }
}
