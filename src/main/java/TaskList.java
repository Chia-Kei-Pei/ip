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
        for (int i = 0; i < size; i++) {
            IO.println(String.format("%d.[%s] %s", i + 1, isMarkList.get(i) ? "X": " ", itemList.get(i)));
        }
    }

//    public void markTask(String item) {
//        int i = itemList.indexOf(item);
//    }
}
