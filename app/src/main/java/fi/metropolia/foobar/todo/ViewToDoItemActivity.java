package fi.metropolia.foobar.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Activity class to view item information
 */
public class ViewToDoItemActivity extends TransitionActivity {
    private ToDoItem item; // item
    private ToDoItemList list; // selected list
    private int i; // item index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_to_do_item);
        // Get extras
        Bundle extras = getIntent().getExtras();
        String listName = extras.getString("ToDoListName");
        i = extras.getInt("ToDoItemIndex");
        list = SelectionList.getInstance().getToDoList(listName);
        item = list.getToDoItem(i);

        getSupportActionBar().setTitle(item.getTitle()); // Set actionbar title

        //Set item values to widgets
        ((TextView)findViewById(R.id.showDesc)).setText(item.getDescription());
        ((Switch)findViewById(R.id.showHighlight)).setChecked(item.isHighlight());
        ((Switch)findViewById(R.id.showDone)).setChecked(item.isDone());
    }

    /**
     * Override onPause to save changes for the selected item
     */
    @Override
    public void onPause() {
        item.setHighlight(((Switch)findViewById(R.id.showHighlight)).isChecked());
        item.setDone(((Switch)findViewById(R.id.showDone)).isChecked());
        super.onPause();
    }
}
