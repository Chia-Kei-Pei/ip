package bert;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BertTest {

    /*
     * Runs all possible cli commands that a user would normally use, expecting no errors.
     */
    @Test
    void run_allCliCommands_noErrors() {
        Bert bert = new Bert("data/todo_list.txt");
        bert.run();

        // write commands to cli

        // read response from cli

    }
}