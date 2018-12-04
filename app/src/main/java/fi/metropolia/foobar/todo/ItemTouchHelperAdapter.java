package fi.metropolia.foobar.todo;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void notifyItemChanged(int position);

}