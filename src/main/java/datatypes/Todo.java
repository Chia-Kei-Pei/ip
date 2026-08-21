package datatypes;

public class Todo {
    protected boolean isMark;
    protected String item;

    public Todo(String item) {
        this.isMark = false;
        this.item = item;
    }

    public void mark() {
        if (isMark) {
            IO.println("Item already marked.");
        } else {
            isMark = true;
            IO.println("Marked Item.");
        }
    }

    public void unmark() {
        if (!isMark) {
            IO.println("Item already unmarked.");
        } else {
            isMark = false;
            IO.println("Unmarked Item.");
        }
    }

    @Override
    public String toString() {
        return String.format("[T][%s] %s", isMark ? "X": " ", item);
    }
}