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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoListRowAdapter extends ArrayAdapter<ToDoItem> {

    ToDoItemList toDoList;

    private int textColor;
    private int backGroundColor;

    private int[] colorValues = {Color.YELLOW,Color.CYAN,Color.RED};

    /**
     * As the check box can cause a live change of ToDo status, formatting split into own method to avoid duplication.
     * @param holder class holding an instance of the current listview's controls
     */
    private void setTextFormatting(ViewHolder holder){
        if ( holder.checkBox.isChecked() ) {
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.textView.setTextColor(Color.LTGRAY);
        } else {

            holder.textView.setPaintFlags(holder.textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

            holder.textView.setTextColor(textColor);
        }
    };


    /**
     * required method in extending ArrayAdapter to work with a dataset
     * @return returns size of the contained ArrayList
     */

    @Override
    public int getCount() {
        return toDoList.getToDoListArray().size();
    }

    /**
     * required method in extending ArrayAdapter to work with a dataset
     * @return returns a requested specific object the ArrayAdapter is adapting.
     */

    @Override
    public ToDoItem getItem(int position) {
        return toDoList.getToDoItem(position);
    }

    /**
     * Method to create the ListView row item's View data for Android to display.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      //  final int index = position; // unneccessary variable

        // viewholder is not allowed to be changed in this context after being first assigned.
        final ViewHolder holder;

        if (convertView == null) {

            // Inflate layout, creates the View from layout file.
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.todo_item_row_layout, parent, false);

            // Setup ViewHolder pattern, object to hold handles to the view's controls for later manipulation.
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
            holder.textView = (TextView) convertView.findViewById(R.id.text);

            // extract default color for text, as haven't found what resource specifies it. Workaround
            textColor = holder.textView.getCurrentTextColor();

            // attaches the holder object to the view to enable usage later reusage
            convertView.setTag(holder);

            //attempting to get backgroundcolor
            backGroundColor = Color.WHITE;
         //   backGroundColor = convertView.getBackground();

        } else {
            // Use cached ViewHolder from the View instead of creating new view to allow us to access controls without using FindViewById
            holder = (ViewHolder) convertView.getTag();
        }


        // set background color to highlighted is current object is marked to be. This can't be changed in liveview
        // so doesn't need to be checked in click handler
        if(toDoList.getToDoItem(position).isHighlight())
        {
        // Set a background color for ListView regular row/item
            SharedPreferences getPref = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);


            convertView.setBackgroundColor(colorValues[getPref.getInt("selection", 0)]);
        }
        else
        {
        // Set the background color for alternate row/item
            convertView.setBackgroundColor(backGroundColor);
        }

        // Get item from data set at the current list position to be used for initial setup and passed into click listener
        final ToDoItem checkItem = toDoList.getToDoItem(position);//getItem(position);

        if (checkItem != null) {
            // Set the views to match the item from your data set
            //          holder.checkBox.setChecked(checkItem.isChecked());

            holder.checkBox.setChecked(checkItem.isDone());
            Log.d(MainActivity.getTAG(), "getView: checked ");
            holder.textView.setText(checkItem.getTitle());
        }

        // set initial text formatting to indicate status of item when view created.
        setTextFormatting(holder);

        // set up listener on the check box control of the listview row to allow live changing of the data
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set the actual todolist item to match current done state from checkbox.
                checkItem.setDone(holder.checkBox.isChecked());

                // update the text formatting of the listrow to match whatever state checkbox changed into
                setTextFormatting(holder);
            }
        });

        // return the converted view to android so it can use it, and cache it to give back to adapter later.
        return convertView;
    }

    /**
     * Constructor for listview row adapter
     * @param context
     * @param resource
     * @param toDoList the ToDolist to be displayed inside the listview.
     */
    public ToDoListRowAdapter(Context context, int resource  , ToDoItemList toDoList) {
            super(context, resource);
            this.toDoList = toDoList;
    }

    // ViewHolder pattern acts as a cache for views inside row view to use for setTag to be able to get back without finding by id, more efficient
    private class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

}
