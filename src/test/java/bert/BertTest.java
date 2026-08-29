package bert;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BertTest {

    /*
     * Runs all possible cli commands that a user would normally use, expecting no errors.
     */
    @Test
    void run_allCliCommands_noErrors() {
        String simulatedInput = "todo read book\nbye\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Bert bert = new Bert("data/todo_list.txt", in, out);
        bert.run();

        // write commands to cli

        // read response from cli

    }
}