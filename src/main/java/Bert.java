import java.util.ArrayList;

public class Bert {
    public static void main(String[] args) {
        String userPrompt;
        ArrayList<String> tasks = new ArrayList<>();

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
                    listTasks(tasks);
                    break;
                default:
                    addTask(userPrompt, tasks);
            }
        }
    }

    public static void addTask(String item, ArrayList<String> tasks) {
        tasks.add(item);
        IO.println("\n------------------------------------------------------------");
        IO.println(String.format("added: %s", item));
    }

    public static void listTasks(ArrayList<String> tasks) {
        IO.println("\n------------------------------------------------------------");
        for (int i = 0; i < tasks.size(); i++) {
            IO.println(String.format("%d. %s", i + 1, tasks.get(i)));
        }
    }
}
