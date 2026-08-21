import java.util.ArrayList;

public class Bert {
    public static void main(String[] args) {
        String userPrompt;
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
            userPrompt = IO.readln("> ");
            switch (userPrompt) {
                case "bye":
                case "exit":
                case "quit":
                    IO.println("\n------------------------------------------------------------");
                    IO.println("Goodbye.");
                    return;
                case "list":
                    tasks.listTasks();
                    break;
//                case "mark":
//                    markTask(item, tasks);
//                    break;
//                case "unmark":
//                    unmarkTask(item, tasks);
//                    break;
                default:
                    tasks.addTask(userPrompt);
            }
        }
    }

//    public static void markTask(String item, ArrayList<String> tasks) {
//        IO.println("\n------------------------------------------------------------");
//        tasks.
//    }
}
