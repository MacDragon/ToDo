package fi.metropolia.foobar.todo;

class SelectionList {
    private static final SelectionList ourInstance = new SelectionList();

    static SelectionList getInstance() {
        return ourInstance;
    }

    private SelectionList() {
    }
}
