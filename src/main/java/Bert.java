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
            StringBuilder itemBuilder;
            String item;
            String byDate;
            String fromDate;
            String toDate;
            int i;

            switch (command[0]) {
                case "todo":
                    itemBuilder = new StringBuilder();
                    for (i = 1; i < command.length; i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    item = itemBuilder.toString().trim();
                    IO.println("todo item: " + item);
                    break;
                case "deadline":
                    i = 1;
                    itemBuilder = new StringBuilder();
                    for (; !command[i].contentEquals("/by") ; i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    item = itemBuilder.toString().trim();
                    IO.println("deadline item: " + item);

                    i++;
                    itemBuilder = new StringBuilder();
                    for (; i < command.length; i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    byDate = itemBuilder.toString().trim();
                    IO.println("byDate: " + byDate);
                    break;
                case "event":
                    i = 1;
                    itemBuilder = new StringBuilder();
                    for (; !command[i].contentEquals("/from"); i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    item = itemBuilder.toString().trim();
                    IO.println("event item: " + item);

                    i++;
                    itemBuilder = new StringBuilder();
                    for (; !command[i].contentEquals("/to"); i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    fromDate = itemBuilder.toString().trim();
                    IO.println("fromDate: " + fromDate);

                    i++;
                    itemBuilder = new StringBuilder();
                    for (; i < command.length; i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    toDate = itemBuilder.toString().trim();
                    IO.println("toDate: " + toDate);
                    break;
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
