import java.util.ArrayList;

public class Bert {
    public static void main(String[] args) {
        String[] command;
        TaskList tasks = new TaskList();

        String banner = """
 ____     ___  ____  ______
|    \\   /  _]|    \\|      |
|  o  ) /  [_ |  D  )      |
|     ||    _]|    /|_|  |_|
|  O  ||   [_ |    \\  |  | 
|     ||     ||  .  \\ |  | 
|_____||_____||__|\\_| |__| 
""";
        IO.println("\n------------------------------------------------------------");
        IO.println(banner);
        IO.println("I am  B E R T.");
        IO.println("What do you need?");

        while (true) {
            IO.println("\n------------------------------------------------------------");
            String userPrompt = IO.readln("> ");
            command = userPrompt.split(" ");
            switch (command[0]) {
                case "bye":
                case "exit":
                case "quit":
                    IO.println("\n------------------------------------------------------------");
                    IO.println("Goodbye.");
                    return;
                case "list":
                    tasks.listTasks();
                    break;
                case "mark":
                    tasks.markTask(Integer.parseInt(command[1]));
                    break;
                case "unmark":
                    tasks.unmarkTask(Integer.parseInt(command[1]));
                    break;
                default:
                    tasks.addTask(userPrompt);
            }
        }
    }
}
