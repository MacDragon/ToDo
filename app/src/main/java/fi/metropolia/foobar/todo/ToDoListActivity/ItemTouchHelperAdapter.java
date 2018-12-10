package fi.metropolia.foobar.todo.ToDoListActivity;


/**
 *  interface to define methods needed for touch interaction with Recycle View
 */

public interface ItemTouchHelperAdapter {

    /**
     * defines method to handle moving item to different location in Recycle View
     * @param fromPosition initial index position of item
     * @param toPosition new index position of item
     * @return return success
     */

    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * defines method to remove item from Recycle view
     * @param position index position of item to remove
     */

    void onItemDismiss(int position);

    /**
     * defines method to notify Recycle view that current item has changed.
     * @param position index position of item changed
     */

    void notifyItemChanged(int position);

}