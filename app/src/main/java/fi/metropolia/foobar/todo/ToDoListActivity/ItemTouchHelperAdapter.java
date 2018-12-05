package fi.metropolia.foobar.todo.ToDoListActivity;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void notifyItemChanged(int position);

}