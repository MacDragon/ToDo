package fi.metropolia.foobar.todo;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

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
        return true;
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
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if ( direction == 16) {
            // confirm deletion here
           adapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
        else if ( direction == 32 ){

            // open editor instead.
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
         //   Log.d(MainActivity.getTAG(), "onLongClick view: " + this.item);
            Intent nextActivity = new Intent(((ToDoListRowAdapter.ToDoItemViewHolder)viewHolder).getView().getContext(), ToDoItemEditorActivity.class);
            // pass editor the listname and index
            nextActivity.putExtra("ToDoItemIndex", viewHolder.getAdapterPosition());
            nextActivity.putExtra("ToDoListName", list.getListName());
            ((ToDoListRowAdapter.ToDoItemViewHolder)viewHolder).getView().getContext().startActivity(nextActivity);

        }
    }

}