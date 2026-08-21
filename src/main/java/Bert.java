import datatypes.Deadline;
import datatypes.Event;
import datatypes.Todo;

import java.util.ArrayList;

public class Bert {
    public static void main(String[] args) {
        String[] command;
        ArrayList<Todo> itemList = new ArrayList<>();

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
            IO.println("\n------------------------------------------------------------");
            command = userPrompt.split(" ");
            StringBuilder itemBuilder;
            String item;

            if (command[0].contentEquals("todo")) {
                itemBuilder = new StringBuilder();
                for (int i = 1; i < command.length; i++) {
                    itemBuilder.append(" ");
                    itemBuilder.append(command[i]);
                }
                item = itemBuilder.toString().trim();

                Todo todo = new Todo(item);
                itemList.add(todo);
                IO.println("Added todo");
                IO.println(todo);
            } else if (command[0].contentEquals("deadline")) {
                int i = 1;
                itemBuilder = new StringBuilder();
                for (; !command[i].contentEquals("/by"); i++) {
                    itemBuilder.append(" ");
                    itemBuilder.append(command[i]);
                }
                item = itemBuilder.toString().trim();

                i++;
                itemBuilder = new StringBuilder();
                for (; i < command.length; i++) {
                    itemBuilder.append(" ");
                    itemBuilder.append(command[i]);
                }
                String byDate = itemBuilder.toString().trim();

                Deadline deadline = new Deadline(item, byDate);
                itemList.add(deadline);
                IO.println("Added deadline");
                IO.println(deadline);
            } else if (command[0].contentEquals("event")) {
                int i = 1;
                itemBuilder = new StringBuilder();
                for (; !command[i].contentEquals("/from"); i++) {
                    itemBuilder.append(" ");
                    itemBuilder.append(command[i]);
                }
                item = itemBuilder.toString().trim();

                i++;
                itemBuilder = new StringBuilder();
                for (; !command[i].contentEquals("/to"); i++) {
                    itemBuilder.append(" ");
                    itemBuilder.append(command[i]);
                }
                String fromDate = itemBuilder.toString().trim();

                i++;
                itemBuilder = new StringBuilder();
                for (; i < command.length; i++) {
                    itemBuilder.append(" ");
                    itemBuilder.append(command[i]);
                }
                String toDate = itemBuilder.toString().trim();

                Event event = new Event(item, fromDate, toDate);
                itemList.add(event);
                IO.println("Added event");
                IO.println(event);
            } else if (command[0].contentEquals("bye")
                    || command[0].contentEquals("exit")
                    || command[0].contentEquals("quit")) {
                IO.println("Goodbye.");
                return;
            } else if (command[0].contentEquals("list")) {
                if (itemList.size() == 0) {
                    IO.println("List is empty.");
                    return;
                }

                for (int i = 0; i < itemList.size(); i++) {
                    IO.println(String.format("%d.%s", i + 1, itemList.get(i).toString()));
                }
            } else if (command[0].contentEquals("mark")) {
                int i = Integer.parseInt(command[1]) - 1;
                itemList.get(i).mark();
                IO.println(String.format("%d.%s", i + 1, itemList.get(i).toString()));
            } else if (command[0].contentEquals("unmark")) {
                int i = Integer.parseInt(command[1]) - 1;
                itemList.get(i).unmark();
                IO.println(String.format("%d.%s", i + 1, itemList.get(i).toString()));
            } else {
                IO.println("ERROR!");
            }
        }
    }
}
