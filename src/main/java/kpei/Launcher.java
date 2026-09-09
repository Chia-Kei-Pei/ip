package kpei;

import javafx.application.Application;

import kpei.datatypes.TaskList;
import kpei.storage.Storage;
import kpei.ui.Cli;
import kpei.ui.Gui;

import javax.swing.*;

/**
 * Entry point that initializes core resources and launches BERT in CLI or GUI mode.
 */
public class Launcher {

    private static final String CLI_FLAG = "--cli";
    private static final String DEFAULT_STORAGE_PATH = "data/todo_list_1.txt";

    /**
     * Main method deciding whether to launch the CLI or GUI version of BERT.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        boolean isCli = false;
        if (args != null) {
            for (String arg : args) {
                if (CLI_FLAG.equalsIgnoreCase(arg.trim())) {
                    isCli = true;
                    break;
                }
            }
        }

        if (isCli) {
            Storage storage = new Storage(DEFAULT_STORAGE_PATH);
            TaskList taskList = new TaskList();
            Cli cli = new Cli();
            Bert bert = new Bert(storage, taskList, cli);
            cli.run(bert);
        } else {
            Gui.initDependencies(DEFAULT_STORAGE_PATH);
            Application.launch(Gui.class, args);
        }
    }
}
