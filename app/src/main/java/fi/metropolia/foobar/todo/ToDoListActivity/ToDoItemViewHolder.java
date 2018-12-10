package fi.metropolia.foobar.todo.ToDoListActivity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import fi.metropolia.foobar.todo.*;


/**
 * ViewHolder class object to store the active state of a Recycler View row.
 */
public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final CheckBox checkBox;
    private final ImageView dragHandle;
    private final View view;
    private Context context;
    private ToDoItem item;
    private ToDoItemList list;

    /**
     * returns the current viewHolders view handle.
     * @return returns the current ViewHolders actual view handle, in order to set background data.
     */
    public View getView() {
        return view;
    }

    /**
     * As the check box can cause a live change of ToDostatus, formatting split into own method to avoid duplication.
     */
    private void setTextFormatting(){
        // if checkbox is checked then set the textview to show dimmed struck through text to show status.
        if ( checkBox.isChecked() ) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textView.setTextColor(Color.LTGRAY);
        } else { // checkbox not checked, set textformatting to defaults.
            textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.tab_indicator_text));
        }
    }

    /**
     * constructor for RecyclerView's item ViewHolder, sets up click event listeners and
     * @param context context of caller
     * @param view callers view
     */
    public ToDoItemViewHolder(Context context, View view) {
        super(view);

        // store parameters for later use.
        this.context = context;
        this.view = view;

        final Context localContext = context;
        dragHandle = (ImageView) itemView.findViewById(R.id.handle);


        // get handle to view's textview in order to setup clicklisteners for short and long clicks to open viewer
        // and editor activities.
        textView = (TextView) view.findViewById(R.id.rowText);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent nextActivity = new Intent(localContext, ToDoItemEditorActivity.class);
                // pass editor the listname and index
                nextActivity.putExtra("ToDoItemIndex", getAdapterPosition());
                nextActivity.putExtra("ToDoListName", list.getListName());
                localContext.startActivity(nextActivity);
                return true;
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextActivity = new Intent(view.getContext(), ViewToDoItemActivity.class);
                // pass viewer the listname and index
                nextActivity.putExtra("ToDoItemIndex", getAdapterPosition());
                nextActivity.putExtra("ToDoListName", list.getListName());
                localContext.startActivity(nextActivity);

            }
        });

        // get handle to the view's checkbox and add click listener to it to respond and update dataset to changes.
        checkBox = (CheckBox) view.findViewById(R.id.check_box);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the actual todolist item to match current done state from checkbox.
                item.setDone(checkBox.isChecked());
                // update the text formatting of the listrow to match whatever state checkbox changed into
                setTextFormatting();
            }
        });
    }

    /**
     * binds the viewHolder's items to the underlying item data and sets up touch listener events
     * @param list list from which to bind item data
     * @param position position in list of data to bind
     * @param dragListener handle to class dealing with touch event data
     */
    public void bindToDoItemViewHolder(ToDoItemList list, int position, final DragListener dragListener) {
        // colour definitions for highlight selection to match selector
        // should ideally be refined into object with colour names
        int[] colorValues = {Color.YELLOW,Color.CYAN,Color.GREEN};

        // set our dataset items.
        this.item = list.getToDoItem(position);;
        this.list = list;

        // store handle to this view to pass into drag event handler.
        final ToDoItemViewHolder handle = this;

        // set current content of view items to match data set.
        checkBox.setChecked(item.isDone());


        String rowText = item.getTitle();
        if ( !item.getDescription().isEmpty()) { rowText = rowText + " ..."; }

        textView.setText(rowText);

        // set state of view background defined by objects highlight status.
        if(item.isHighlight())
        {
            // set the background colour to our selected highlight colour
            SharedPreferences getPref = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            view.setBackgroundColor(colorValues[getPref.getInt("selection", 0)]);
        }
        else
        {
            // Set the background color back to white
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.background_light));
        }

        // set the text formatting to match data state.
        setTextFormatting();

        // define listener for touch events on the view to respond to swipes and drags.
        dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(handle); // tell the Activity to start drag event.
                }
                return false;
            }
        });
    }

}
