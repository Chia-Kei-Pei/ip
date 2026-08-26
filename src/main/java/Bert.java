import datatypes.Deadline;
import datatypes.Event;
import datatypes.Todo;
import datatypes.TodoList;
import exceptions.BertException;
import exceptions.UnknownCommandException;

public class Bert {
    public static void main(String[] args) throws IllegalArgumentException {
        String[] command;
        TodoList itemList = new TodoList();

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

            try {
                if (command[0].contentEquals("todo")) {
                    int i = 1;
                    itemBuilder = new StringBuilder();

                    if (i > command.length - 1) {
                        throw new IllegalArgumentException("Name of todo should not be empty");
                    }
                    for (; i < command.length; i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    item = itemBuilder.toString().trim();

                    Todo todo = new Todo(item);
                    itemList.add(todo);
                } else if (command[0].contentEquals("deadline")) {
                    int i = 1;
                    itemBuilder = new StringBuilder();

                    if (i > command.length - 1) {
                        throw new IllegalArgumentException("Name of deadline should not be empty");
                    }
                    for (; i < command.length; i++) {
                        if (command[i].contentEquals("/by")) {
                            break;
                        }
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    item = itemBuilder.toString().trim();

                    i++;
                    itemBuilder = new StringBuilder();

                    if (i > command.length - 1) {
                        throw new IllegalArgumentException("ByDate of deadline should not be empty");
                    }
                    for (; i < command.length; i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    String byDate = itemBuilder.toString().trim();

                    Deadline deadline = new Deadline(item, byDate);
                    itemList.add(deadline);
                } else if (command[0].contentEquals("event")) {
                    int i = 1;
                    itemBuilder = new StringBuilder();
                    if (i > command.length - 1) {
                        throw new IllegalArgumentException("Name of event should not be empty");
                    }
                    for (; i < command.length; i++) {
                        if (command[i].contentEquals("/from")) {
                            break;
                        }
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    item = itemBuilder.toString().trim();

                    i++;
                    itemBuilder = new StringBuilder();

                    if (i > command.length - 1) {
                        throw new IllegalArgumentException("FromDate of event should not be empty");
                    }
                    for (; i < command.length; i++) {
                        if (command[i].contentEquals("/to")) {
                            break;
                        }
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    String fromDate = itemBuilder.toString().trim();

                    i++;
                    itemBuilder = new StringBuilder();

                    if (i > command.length - 1) {
                        throw new IllegalArgumentException("ToDate of event should not be empty");
                    }
                    for (; i < command.length; i++) {
                        itemBuilder.append(" ");
                        itemBuilder.append(command[i]);
                    }
                    String toDate = itemBuilder.toString().trim();

                    Event event = new Event(item, fromDate, toDate);
                    itemList.add(event);
                } else if (command[0].contentEquals("bye")
                        || command[0].contentEquals("exit")
                        || command[0].contentEquals("quit")) {
                    IO.println("Goodbye.");
                    return;
                } else if (command[0].contentEquals("list")) {
                    itemList.printList();
                } else if (command[0].contentEquals("mark")) {
                    int i = Integer.parseInt(command[1]);
                    itemList.mark(i);
                } else if (command[0].contentEquals("unmark")) {
                    int i = Integer.parseInt(command[1]);
                    itemList.unmark(i);
                } else if (command[0].contentEquals("delete") || command[0].contentEquals("remove")) {
                    int i = Integer.parseInt(command[1]);
                    itemList.remove(i);
                } else {
                    throw new UnknownCommandException(command[0]);
                }
            } catch (BertException e) {
                IO.println(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                IO.println(e);
            } catch (IllegalArgumentException e) {
                IO.println(e.getMessage());
            }
        }
    }
}
