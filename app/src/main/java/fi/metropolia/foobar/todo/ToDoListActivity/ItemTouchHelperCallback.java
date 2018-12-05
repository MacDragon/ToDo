package fi.metropolia.foobar.todo.ToDoListActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import fi.metropolia.foobar.todo.ToDoItem;
import fi.metropolia.foobar.todo.ToDoItemEditorActivity;
import fi.metropolia.foobar.todo.ToDoItemList;

//https://developer.android.com/reference/android/support/v7/widget/helper/ItemTouchHelper.Callback
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;
    private ToDoItem item;
    private ToDoItemList list;

    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter, ToDoItemList list) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false; //true;  disable default drag and drop in order to use nicer handles.
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

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
            // confirm deletion here
                AlertDialog.Builder confirmDelete = new AlertDialog.Builder(((ToDoItemViewHolder)viewHolder).getView().getContext());
                confirmDelete.setTitle("Delete item");
                confirmDelete.setMessage("Are you sure you want to delete this item?");
                confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.onItemDismiss(viewHolder.getAdapterPosition());
                    }
                });
                confirmDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }

                    });
                confirmDelete.show();

                // need to catch cancel and restore item

        }  // swipe to
        else if ( direction == ItemTouchHelper.END ){

            // open editor instead.
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
         //   Log.d(MainActivity.getTAG(), "onLongClick view: " + this.item);
            Intent nextActivity = new Intent(((ToDoItemViewHolder)viewHolder).getView().getContext(), ToDoItemEditorActivity.class);
            // pass editor the listname and index
            nextActivity.putExtra("ToDoItemIndex", viewHolder.getAdapterPosition());
            nextActivity.putExtra("ToDoListName", list.getListName());
            ((ToDoItemViewHolder)viewHolder).getView().getContext().startActivity(nextActivity);

        }
    }

}