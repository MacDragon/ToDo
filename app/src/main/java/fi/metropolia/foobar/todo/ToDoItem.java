package fi.metropolia.foobar.todo;

public class ToDoItem {
    private String title;
    private String description;
    private boolean highlight;
    private boolean done;


    /**
     * Constructor to create new complete ToDo item
     *
     * @param title name of ToDoList item
     * @param description detailed description of ToDo item
     * @param highlight should item be highlighted
     * @param done is item completed
     */
    public ToDoItem(String title, String description, boolean highlight, boolean done) {
        this.title = title;
        this.description = description;
        this.highlight = highlight;
        this.done = done;
    }

    /**
     * Constructor to create a new ToDo item
     * @param title name of ToDoList item
     * @param description detailed description of ToDo item
     * @param highlight should item be highlighted
     */
    public ToDoItem(String title, String description, boolean highlight) {
        this(title, description, highlight, false);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isHighlight() {
        return highlight;
    }
}
