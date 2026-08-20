public class Bert {
    public static void main(String[] args) {
        String userPrompt;

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
        IO.println("What'd you need?");

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
                default:
                    IO.println("\n------------------------------------------------------------");
                    IO.println(userPrompt);
            }
        }
    }
}
