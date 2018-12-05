package fi.metropolia.foobar.todo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final CheckBox checkBox;
    private final ImageView dragHandle;
    private final View view;
    private Context context;
    private ToDoItem item;
    private ToDoItemList list;
    private int textColor;
    private int backGroundColor;

    // colour definitions for highlight selection to match selector
    private int[] colorValues = {Color.YELLOW,Color.CYAN,Color.RED};


    public View getView() {
        return view;
    }

    /**
     * As the check box can cause a live change of ToDo status, formatting split into own method to avoid duplication.
     */
    private void setTextFormatting(){
        if ( checkBox.isChecked() ) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            textView.setTextColor(Color.LTGRAY);
        } else {

            textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            textView.setTextColor(textColor);
        }
    };


    public ToDoItemViewHolder(Context context, View view) {
        super(view);
        Log.d(MainActivity.getTAG(), "creating holder");
        this.context = context;
        final Context localContext = context;
        dragHandle = (ImageView) itemView.findViewById(R.id.handle);


        textView = (TextView) view.findViewById(R.id.rowText);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //getLayoutPosition();
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
                //getLayoutPosition();

                Log.d(MainActivity.getTAG(), "onClick view: ");
                Intent nextActivity = new Intent(view.getContext(), ViewToDoItemActivity.class);
                // pass viewer the listname and index
                nextActivity.putExtra("ToDoItemIndex", getAdapterPosition()); // getLayoutPosition()?
                nextActivity.putExtra("ToDoListName", list.getListName());
                view.getContext().startActivity(nextActivity);

            }
        });




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


        this.view = view;
        //   view.setOnClickListener(this);
    }

    public void bindToDoItemViewHolder(ToDoItemList list,  int position, final DragListener dragListener) {
        this.item = list.getToDoItem(position);;
        this.list = list;
        final ToDoItemViewHolder handle = this;
        checkBox.setChecked(item.isDone());
        textView.setText(item.getTitle());
        backGroundColor = Color.WHITE;
        if(item.isHighlight())
        {
            // Set a background color for ListView regular row/item
            view.setBackgroundColor(Color.YELLOW);

            SharedPreferences getPref = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
            view.setBackgroundColor(colorValues[getPref.getInt("selection", 0)]);
        }
        else
        {
            // Set the background color for alternate row/item
            view.setBackgroundColor(backGroundColor);
        }
        textColor = textView.getCurrentTextColor();
        setTextFormatting();
        dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(handle);
                }
                return false;
            }
        });
        Log.d(MainActivity.getTAG(), "binding item: " + this.item);
    }

    /*    @Override
        public void onClick(View view) {
            if (this.item != null) {
                Log.d(MainActivity.getTAG(), "onClick view: " + this.item);
                Intent nextActivity = new Intent(view.getContext(), ViewToDoItemActivity.class);
                // pass viewer the listname and index
                nextActivity.putExtra("ToDoItemIndex", getAdapterPosition());
                nextActivity.putExtra("ToDoListName", toDoList.getListName());
                view.getContext().startActivity(nextActivity);
            }
        } */

}
