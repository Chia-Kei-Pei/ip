package datatypes;

public class Todo {
    public boolean isMark;
    public String item;

    public Todo(String item) {
        this.isMark = false;
        this.item = item;
    }
}