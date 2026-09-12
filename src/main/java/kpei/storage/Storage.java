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
            for (String line : Files.readAllLines(path)) {
                loadTask(taskList, line);
            }
        } catch (IOException e) {
            throw new BertException("Warning: Unable to load data from " + filePath + " (" + e.getMessage() + ")", e);
        }
    }

    /**
     * Parses a storage line and adds its task to the provided list when valid.
     *
     * @param taskList The list receiving the restored task.
     * @param line A line read from the storage file.
     * @throws BertException If a recognized task has invalid data.
     */
    private void loadTask(TaskList taskList, String line) throws BertException {
        Task task = parseStoredTask(line);
        if (task != null) {
            taskList.add(task);
        }
    }

    /**
     * Parses one storage line into a task.
     *
     * @param line A line read from the storage file.
     * @return The parsed task, or {@code null} for blank, incomplete, or unrecognized lines.
     * @throws BertException If a recognized task has invalid data.
     */
    private Task parseStoredTask(String line) throws BertException {
        if (line.isBlank()) {
            return null;
        }

        String[] fields = line.split("\\s*\\|\\s*");
        if (fields.length < 3) {
            return null;
        }

        String type = fields[0].trim();
        boolean isMarked = Boolean.parseBoolean(fields[1].trim()) || fields[1].trim().equals("1");
        String description = fields[2].trim();

        return switch (type) {
            case "todo" -> TaskParser.parseTask(isMarked, description);
            case "deadline" -> parseStoredDeadline(line, fields, isMarked, description);
            case "event" -> parseStoredEvent(line, fields, isMarked, description);
            default -> null;
        };
    }

    /**
     * Parses a stored deadline when its required due-date field is present.
     *
     * @param line The original storage line.
     * @param fields The fields extracted from the storage line.
     * @param isMarked Whether the deadline is completed.
     * @param description The deadline description.
     * @return The parsed deadline, or {@code null} when its due date is absent.
     * @throws BertException If the deadline fields are invalid.
     */
    private Task parseStoredDeadline(String line, String[] fields, boolean isMarked, String description)
            throws BertException {
        if (fields.length < 4) {
            return null;
        }

        try {
            return TaskParser.parseDeadline(isMarked, description, fields[3].trim());
        } catch (BertException e) {
            throw new BertException("Warning: Skipping task with invalid deadline in " + filePath + ": " + line, e);
        }
    }

    /**
     * Parses a stored event when its required start and end fields are present.
     *
     * @param line The original storage line.
     * @param fields The fields extracted from the storage line.
     * @param isMarked Whether the event is completed.
     * @param description The event description.
     * @return The parsed event, or {@code null} when either date is absent.
     * @throws BertException If the event fields are invalid.
     */
    private Task parseStoredEvent(String line, String[] fields, boolean isMarked, String description)
            throws BertException {
        if (fields.length < 5) {
            return null;
        }

        try {
            return TaskParser.parseEvent(isMarked, description, fields[3].trim(), fields[4].trim());
        } catch (BertException e) {
            throw new BertException("Warning: Skipping task with invalid event dates in " + filePath + ": " + line, e);
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
            for (Task task : taskList.getTasks()) {
                lines.add(task.toFileFormat());
            }
            assert lines.size() == taskList.size()
                    : "Each task should produce exactly one storage line";

            Files.write(path, lines);
        } catch (IOException e) {
            throw new BertException("Warning: Unable to save data to " + filePath + " (" + e.getMessage() + ")", e);
        }
    }

    /**
     * Returns the file name of the storage file path.
     *
     * @return The storage file name.
     */
    public String getFileName() {
        return Path.of(filePath).getFileName().toString();
    }
}
