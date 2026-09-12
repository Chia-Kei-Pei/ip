package kpei;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kpei.datatypes.Task;
import kpei.datatypes.TaskList;
import kpei.storage.Storage;
import kpei.ui.Cli;

class BertTest {

    @TempDir
    Path tempDir;

    private String runCliWithInput(String testDataFilePath, String simulatedInput) {
        InputStream input = new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8));
        StringBuilder output = new StringBuilder();

        Storage storage = new Storage(testDataFilePath);
        TaskList taskList = new TaskList("task_list.txt");
        Cli cli = new Cli(msg -> output.append(msg));
        CommandCenter commandCenter = new CommandCenter(storage, taskList, cli);

        cli.run(input, commandCenter);

        return output.toString();
    }

    /*
     * Runs all possible cli commands that a user would normally use, expecting no errors.
     */
    @Test
    void run_allCliCommands_noErrors() {
        String simulatedInput = String.join(System.lineSeparator(),
                "todo \"Clean my room\"",
                "deadline \"Math homework\" by \"2026-08-29 16:00\"",
                "event \"nerd con\" from \"2027-05-31\" to \"2027-06-10\"",
                "list",
                "mark 1",
                "unmark 1",
                "delete 1",
                "list",
                "exit"
        ) + System.lineSeparator();

        String testDataFilePath = tempDir.resolve("task_list.txt").toString();
        String output = runCliWithInput(testDataFilePath, simulatedInput);
        String taskType = new Task("Clean my room").getType();

        assertTrue(output.contains("Added " + taskType));
        assertTrue(output.contains("[" + taskType + "][ ] Clean my room"));
        assertTrue(output.contains("Added deadline"));
        assertTrue(output.contains("[deadline][ ] Math homework (by: Aug 29 2026 at 16:00)"));
        assertTrue(output.contains("Added event"));
        assertTrue(output.contains("[event][ ] nerd con (from: May 31 2027, to: Jun 10 2027)"));
        assertTrue(output.contains("Marked " + taskType));
        assertTrue(output.contains("1.[" + taskType + "][X] Clean my room"));
        assertTrue(output.contains("Unmarked " + taskType));
        assertTrue(output.contains("1.[" + taskType + "][ ] Clean my room"));
        assertTrue(output.contains("Removed " + taskType));
        assertTrue(output.contains("1.[deadline][ ] Math homework (by: Aug 29 2026 at 16:00)"));
        assertTrue(output.contains("2.[event][ ] nerd con (from: May 31 2027, to: Jun 10 2027)"));
        assertTrue(output.contains("Goodbye."));
    }

    @Test
    void findCommand_matchingTasks_displayedSuccessfully() {
        String simulatedInput = String.join(System.lineSeparator(),
                "todo \"Clean my room\"",
                "deadline \"Math homework\" by \"2026-08-29 16:00\"",
                "event \"nerd con\" from \"2027-05-31\" to \"2027-06-10\"",
                "find room",
                "find \"Math homework\"",
                "find MATH",
                "exit"
        ) + System.lineSeparator();

        String testDataFilePath = tempDir.resolve("task_list_find.txt").toString();
        String output = runCliWithInput(testDataFilePath, simulatedInput);
        String taskType = new Task("Clean my room").getType();

        assertTrue(output.contains("Matching tasks:"));
        assertTrue(output.contains("1.[" + taskType + "][ ] Clean my room"));
        assertTrue(output.contains("1.[deadline][ ] Math homework (by: Aug 29 2026 at 16:00)"));
    }

    @Test
    void findCommand_noMatchingTasks_noMatchesMessageDisplayed() {
        String simulatedInput = String.join(System.lineSeparator(),
                "todo \"Clean my room\"",
                "find \"non-existent keyword\"",
                "exit"
        ) + System.lineSeparator();

        String testDataFilePath = tempDir.resolve("task_list_find_empty.txt").toString();
        String output = runCliWithInput(testDataFilePath, simulatedInput);

        assertTrue(output.contains("No matching tasks found."));
    }
}
