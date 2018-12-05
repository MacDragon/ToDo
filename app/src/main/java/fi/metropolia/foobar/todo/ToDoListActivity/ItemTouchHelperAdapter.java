package fi.metropolia.foobar.todo.ToDoListActivity;


/**
 *  interface to define methods needed for touch interaction with Recycle View
 */

public interface ItemTouchHelperAdapter {

    /**
     * defines method to handle moving item to different location in Recycle View
     * @param fromPosition
     * @param toPosition
     * @return
     */

    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * defines method to remove item from Recycle view
     * @param position
     */

    void onItemDismiss(int position);

    /**
     * defines method to notify Recycle view that current item has changed.
     * @param position
     */

    void notifyItemChanged(int position);

}