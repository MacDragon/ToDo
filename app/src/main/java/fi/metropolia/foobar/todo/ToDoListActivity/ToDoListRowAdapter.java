package fi.metropolia.foobar.todo.ToDoListActivity;


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
inspiration from https://traversoft.com/2016/01/31/replace-listview-with-recyclerview/
*/
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fi.metropolia.foobar.todo.MainActivity;
import fi.metropolia.foobar.todo.ToDoItem;
import fi.metropolia.foobar.todo.ToDoItemList;

public class ToDoListRowAdapter extends RecyclerView.Adapter<ToDoItemViewHolder> implements ItemTouchHelperAdapter {

    /**
     *  store the list this adapter is working with.
     */

    private ToDoItemList toDoList;

    /**
     * stored application context for use with
     */
    private Context context;

    /**
     * handle to the resource file used for creating view.
     */
    private int resource;

    private final DragListener dragListener;

    /**
     * returns the RecyclerView's dataset.
     * @return
     */
    public ToDoItemList getList(){
        return toDoList;
    }

    /**
     * method to facilitate responding to request to remove item from list
     * @param position position in the list to remove
     */
    @Override
    public void onItemDismiss(int position) {
        toDoList.remove(position);
        notifyItemRemoved(position);
    }

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
     * required method in extending ArrayAdapter to return it's working dataset size.
     * @return returns size of the contained ArrayList
     */

    @Override
    public int getItemCount() {
        Log.d(MainActivity.getTAG(), "getItemCount: " + toDoList.size());
        return toDoList.size();
    }

    /**
     * Constructor for listview row adapter storing all passed parameters for later use.
     * @param context
     * @param resource
     * @param toDoList the ToDolist to be displayed inside the listview.
     */
    public ToDoListRowAdapter(Context context, int resource, ToDoItemList toDoList , DragListener dragListener ) {
        this.context = context;
        this.toDoList = toDoList;
        this.resource = resource;
        this.dragListener = dragListener;
    }

    /**
     * creates view for an item in RecyclerView using ViewHolder
     * @param viewGroup
     * @param position
     * @return
     */
    @Override
    public ToDoItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        return new ToDoItemViewHolder(context, view);
    }

    /**
     * attaches item data from current todolist to a RecyclerView's item viewHolder.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ToDoItemViewHolder holder, int position) {
        holder.bindToDoItemViewHolder(toDoList, position, dragListener);
    }

}
