package fi.metropolia.foobar.todo.ToDoListActivity;

import android.support.v7.widget.RecyclerView;

/**
 * defines interface for method to start item drag event.
 */

public interface DragListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}