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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;


public class ToDoListRowAdapter extends RecyclerView.Adapter<ToDoListRowAdapter.ToDoItemViewHolder> implements ItemTouchHelperAdapter {

    private ToDoItemList toDoList;
    private int textColor;
    private int backGroundColor;
    private Context context;
    private int resource;

    private final DragListener dragListener;

    /**
     * method to facilitate responding to request to remove item from list
     * @param position position in the list to remove
     */
    @Override
    public void onItemDismiss(int position) {
        toDoList.remove(position);
        notifyItemRemoved(position);
    }

    // colour definitions for highlight selection to match selector
    private int[] colorValues = {Color.YELLOW,Color.CYAN,Color.RED};

    /**
     * method to facilitate moving item  in list in response to drag events.
     * @param fromPosition original position of item
     * @param toPosition updated position of item
     * @return
     */

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        ToDoItem item = toDoList.getToDoItem(fromPosition);
        toDoList.remove(fromPosition);
        toDoList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * required method in extending ArrayAdapter to work with a dataset
     * @return returns size of the contained ArrayList
     */

    @Override
    public int getItemCount() {
        Log.d(MainActivity.getTAG(), "getItemCount: " + toDoList.size());
        return toDoList.size();
    }

    /**
     * Constructor for listview row adapter
     * @param context
     * @param resource
     * @param toDoList the ToDolist to be displayed inside the listview.
     */
    public ToDoListRowAdapter(Context context, int resource , ToDoItemList toDoList, DragListener dragListener) {
        Log.d(MainActivity.getTAG(), "ToDoListRowAdapter: ");
        this.context = context;
        this.toDoList = toDoList;
        this.resource = resource;
        this.dragListener = dragListener;
    }

    @Override
    public ToDoItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);

        // extract default color for text, as haven't found what resource specifies it. Workaround

        Log.d(MainActivity.getTAG(), "creating viewholder item: " + i);
        return new ToDoItemViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(ToDoItemViewHolder holder, int position) {
        final ToDoItemViewHolder internalHolder = holder;
        Log.d(MainActivity.getTAG(), "onBindViewHolder: ");
        final ToDoItem item = toDoList.getToDoItem(position);
        textColor = internalHolder.textView.getCurrentTextColor();
        internalHolder.dragHandle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(internalHolder);
                }
                return false;
            }
        });

        holder.bindToDoItemViewHolder(toDoList, item);
    }


    public class ToDoItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final CheckBox checkBox;
        private final ImageView dragHandle;
        private final View view;
        private Context context;
        private ToDoItem item;
        private ToDoItemList list;

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
                    nextActivity.putExtra("ToDoListName", toDoList.getListName());
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
                    nextActivity.putExtra("ToDoListName", toDoList.getListName());
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

        public void bindToDoItemViewHolder(ToDoItemList list, ToDoItem item) {
            this.item = item;
            this.list = list;
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
            setTextFormatting();
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


}
