import java.util.ArrayList;

public class TaskList {
    private ArrayList<String> itemList;
    private ArrayList<Boolean> isMarkList;
    private int size;

    public TaskList() {
        itemList = new ArrayList<>();
        isMarkList = new ArrayList<>();
        size = 0;
    }

    public void addTask(String item) {
        itemList.add(item);
        isMarkList.add(false);
        size++;
        IO.println("\n------------------------------------------------------------");
        IO.println(String.format("added: %s", item));
    }

    public void listTasks() {
        IO.println("\n------------------------------------------------------------");
        if (size == 0) {
            IO.println("List is empty.");
            return;
        }

        for (int i = 0; i < size; i++) {
            printTask(i);
        }
    }

    public void markTask(int n) {
        IO.println("\n------------------------------------------------------------");
        int i = n - 1;

        if (isMarkList.get(i)) {
            IO.println("Task already marked.");
        } else {
            isMarkList.set(i, true);
            IO.println("Marked task.");
        }

        printTask(i);
    }

    public void unmarkTask(int n) {
        IO.println("\n------------------------------------------------------------");
        int i = n - 1;

        if (!isMarkList.get(i)) {
            IO.println("Task already unmarked.");
        } else {
            isMarkList.set(i, false);
            IO.println("Unmarked task.");
        }

        printTask(i);
    }

    private void printTask(int i) {
        IO.println(String.format("%d.[%s] %s", i + 1, isMarkList.get(i) ? "X": " ", itemList.get(i)));
    }
}
