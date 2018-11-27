package fi.metropolia.foobar.todo;


/* ToDoListRowAdapter // create the custom view for ToDoList entries with check box
        {
        setTextColorByChecked(ViewHolder holder)
        ToDoListRowAdapter(Context context, int resource, ArrayList<ToDoItem> ToDoList)
        getView(int position, View convertView, ViewGroup parent)
        getItemId(int position)
        getItem(int position) ToDoItem
        getCount

        VisaHolder {
        TextView textView
        CheckBox checkBox
        }

        }


*/

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoListRowAdapter extends ArrayAdapter<ToDoItem> {

    private ArrayList<ToDoItem> toDoList;

    @Override
    public int getCount() {
        return toDoList.size();
    }

    @Override
    public ToDoItem getItem(int position) {
        return toDoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int index = position;

        final ViewHolder holder;

    //    if (convertView == null) {

            // Inflate layout
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.todo_item_row_layout, parent, false);

            // Setup ViewHolder
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            holder.textView = (TextView) convertView.findViewById(R.id.text);

            // Store ViewHolder with this row view
            convertView.setTag(holder);

     /*   } else {

            // Use viewHolder - quicker than calling findViewById() on resource every time
            holder = (ViewHolder) convertView.getTag();
        } */

        // Get item from  data set at the current list position
        final ToDoItem checkItem = getItem(position);

        if (checkItem != null) {
            // Set the views to match the item from your data set
            //          holder.checkBox.setChecked(checkItem.isChecked());

            holder.checkBox.setChecked(checkItem.isDone());
            Log.d(MainActivity.getTAG(), "getView: checked ");
            holder.textView.setText(checkItem.getTitle());
        }

        return convertView;
    }

    public ToDoListRowAdapter(Context context, int resource  , ArrayList<ToDoItem> toDoList) {
            super(context, resource);
            this.toDoList = toDoList;
    }

    // ViewHolder acts as a cache for row views
    private class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

}
