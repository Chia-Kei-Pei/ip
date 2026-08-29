package bert.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import bert.datatypes.Deadline;
import bert.datatypes.Event;
import bert.datatypes.Todo;
import bert.datatypes.TodoList;
import bert.exceptions.BertException;
import bert.parser.DateTimeParser;
import bert.ui.Ui;

/**
 * Handles persistent storage of {@link TodoList} tasks to and from a local file.
 * Uses a human-readable pipe-delimited plain text format.
 */
public class Storage {
    private final String filePath;
    private final Ui ui;

    /**
     * Constructs a {@code Storage} handler with a custom file path and {@link Ui} instance.
     *
     * @param filePath Relative or absolute path to the data storage file.
     * @param ui The {@link Ui} instance used for displaying warning messages.
     */
    public Storage(String filePath, Ui ui) {
        this.filePath = filePath;
        this.ui = ui;
    }

    /**
     * Loads tasks from the save file into the provided {@link TodoList}.
     * If the file does not exist, no action is taken and the list remains as-is.
     * Corrupted or unrecognized lines are safely ignored.
     *
     * @param todoList The list to populate with loaded tasks.
     */
    public void load(TodoList todoList) {
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
                    case "todo" -> todoList.add(new Todo(isMarked, description));
                    case "deadline" -> {
                        if (parts.length >= 4) {
                            try {
                                LocalDateTime byDate = DateTimeParser.parse(parts[3].trim());
                                todoList.add(new Deadline(isMarked, description, byDate));
                            } catch (BertException e) {
                                ui.showWarning("Warning: Skipping task with invalid deadline in "
                                        + filePath + ": " + line);
                            }
                        }
                    }
                    case "event" -> {
                        if (parts.length >= 5) {
                            try {
                                LocalDateTime fromDate = DateTimeParser.parse(parts[3].trim());
                                LocalDateTime toDate = DateTimeParser.parse(parts[4].trim());
                                todoList.add(new Event(isMarked, description, fromDate, toDate));
                            } catch (BertException e) {
                                ui.showWarning("Warning: Skipping task with invalid event dates in "
                                        + filePath + ": " + line);
                            }
                        }
                    }
                    default -> {
                        // Ignore unrecognized task types
                    }
                }
            }
        } catch (IOException e) {
            ui.showWarning("Warning: Unable to load data from " + filePath + " (" + e.getMessage() + ")");
        }
    }

    /**
     * Saves all tasks from the specified {@link TodoList} into the storage file.
     * Creates any missing parent directories automatically.
     *
     * @param todoList The list containing tasks to save.
     */
    public void save(TodoList todoList) {
        Path path = Path.of(filePath);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            List<String> lines = new ArrayList<>();
            for (Todo todo : todoList.getTodos()) {
                lines.add(todo.toFileFormat());
            }

            Files.write(path, lines);
        } catch (IOException e) {
            ui.showWarning("Warning: Unable to save data to " + filePath + " (" + e.getMessage() + ")");
        }
    }
}
