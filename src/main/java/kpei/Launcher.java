package kpei;

import javafx.application.Application;

import kpei.ui.Gui;

/**
 * Entry point that determines whether to start BERT in CLI mode or GUI mode based on arguments.
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
            Bert bert = new Bert(DEFAULT_STORAGE_PATH, System.in, System.out, false);
            bert.run();
        } else {
            Application.launch(Gui.class, args);
        }
    }
}
