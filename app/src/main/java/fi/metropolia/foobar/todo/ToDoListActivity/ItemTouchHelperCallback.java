package fi.metropolia.foobar.todo.ToDoListActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import fi.metropolia.foobar.todo.ToDoItemEditorActivity;
import fi.metropolia.foobar.todo.ToDoItemList;


/**
 * helper callback class to implement touch event handling for Recycler View.
 * See package-info.java for further credit in implementation
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    // store the adapter that this helper class is attached to so it can pass events over to it.
    private final ItemTouchHelperAdapter adapter;
    // store the itemlist being handled for easy retrieval of items and name.
    private ToDoItemList list;

    /**
     * Constructor for the callback class for handling drag/swipe events on Recycle view
     * @param adapter requires the adapter it is acting as a helper for, to pass events to
     * @param list the item list the adapter is adapting.
     */

    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter, ToDoItemList list) {
        this.list = list;
        this.adapter = adapter;
    }

    /**
     * Override method to disable Recycler View's default drag and drop functionality, so that we can use quick
     * drag and drop from handle instead of long press.
     * @return returns false in order to disable the function.
     */

    @Override
    public boolean isLongPressDragEnabled() {
        return false; //true;  disable default drag and drop in order to use nicer handles.
    }

    /**
     * Method to enable Recycler View's default swipe handling.
     * @return true to enable default swipe handling.
     */

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * define what touch events the Recycler View can respond to.
     * @param recyclerView the Recycler we will be responding to.
     * @param viewHolder the holder for the current Recycler row we are responding to.
     * @return the flag definition of what to respond to.
     */

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        // swipe towards left to delete
        if ( direction == ItemTouchHelper.START) {
            // confirm deletion here by creating dialog box to inform user with message and requesting confirmation
                AlertDialog.Builder confirmDelete = new AlertDialog.Builder(((ToDoItemViewHolder)viewHolder).getView().getContext());
                confirmDelete.setTitle("Delete item");
                confirmDelete.setMessage("Are you sure you want to delete this item?");
                // add the OK/cancel buttons to dialog along with their click actions
                confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user wanted to delete item, tell adapter to remove it.
                        adapter.onItemDismiss(viewHolder.getAdapterPosition());
                    }
                });
                confirmDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user didn't want to delete item after all, tell adapter that item has changed to update view.
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }

                    });
                // show the dialog
                confirmDelete.show();
        }  // swipe to
        else if ( direction == ItemTouchHelper.END ){

            // open editor instead, get the current item index to pass in intent to editor
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
         //   Log.d(MainActivity.getTAG(), "onLongClick view: " + this.item);
            // set up intent to editor, using the holder's context
            Intent nextActivity = new Intent(((ToDoItemViewHolder)viewHolder).getView().getContext(), ToDoItemEditorActivity.class);
            // pass editor the listname and index in Extras
            nextActivity.putExtra("ToDoItemIndex", viewHolder.getAdapterPosition());
            nextActivity.putExtra("ToDoListName", list.getListName());
            // call the  editor activity with the holder's context
            ((ToDoItemViewHolder)viewHolder).getView().getContext().startActivity(nextActivity);

        }
    }

}