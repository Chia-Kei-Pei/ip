import datatypes.Todo;

import java.util.ArrayList;

public class ItemList {
    private ArrayList<Todo> itemList;

    public ItemList() {
        itemList = new ArrayList<>();
    }

    public void listItems() {
        IO.println("\n------------------------------------------------------------");
        if (itemList.size() == 0) {
            IO.println("List is empty.");
            return;
        }

        for (int i = 0; i < itemList.size(); i++) {
            printItems(i);
        }
    }

    public void markItems(int n) {
        IO.println("\n------------------------------------------------------------");
        int i = n - 1;
        itemList.get(i).mark();
        printItems(i);
    }

    public void unmarkItems(int n) {
        IO.println("\n------------------------------------------------------------");
        int i = n - 1;
        itemList.get(i).unmark();
        printItems(i);
    }

    private void printItems(int i) {
        IO.println(String.format("%d.%s", i + 1, itemList.get(i).toString()));
    }
}
