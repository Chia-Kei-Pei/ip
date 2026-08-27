package ui;

public class Ui {
    private static final String BANNER = """
 ____     ___  ____  ______
|    \\   /  _]|    \\|      |
|  o  ) /  [_ |  D  )      |
|     ||    _]|    /|_|  |_|
|  O  ||   [_ |    \\  |  | 
|     ||     ||  .  \\ |  | 
|_____||_____||__|\\_| |__| 
""";
    private static final String HORIZONTAL_LINE = "____________________________________________________________";

    public Ui() {

    }

    public void greeting() {
        IO.println(BANNER);
        IO.println("I am  BERT.");
        IO.println("What do you need?");
    }

    public void farewell() {
        IO.println("Goodbye.");
    }

    public void showLine() {
        IO.println(HORIZONTAL_LINE);
    }

    public String userPrompt() {
        return IO.readln("> ");
    }

    public void showError(String msg) {
        IO.println(msg);
    }
}
