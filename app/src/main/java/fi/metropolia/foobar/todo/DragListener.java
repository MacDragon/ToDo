package fi.metropolia.foobar.todo;

import android.support.v7.widget.RecyclerView;

public interface DragListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}