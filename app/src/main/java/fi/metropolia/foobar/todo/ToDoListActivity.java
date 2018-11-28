package fi.metropolia.foobar.todo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Activity to show a todolist called from main selector activity
 */

public class ToDoListActivity extends AppCompatActivity {

    ToDoItemList toDoItemList;
    Parcelable state = null;

    /**
     * Returns the listrow adapter saved earlier for data refresh
     *
     * @return
     */

    public ToDoListRowAdapter getAdapter() {
        return adapter;
    }

    private ToDoListRowAdapter adapter;

    /**
     * Setup ToDoList when activity called, using extra info to read list name.
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        //create dummy list and items

        Bundle extras = getIntent().getExtras();
        String listName = extras.getString("listName");
        Log.d(MainActivity.getTAG(), "onCreate: " + listName);
        Log.d(MainActivity.getTAG(), "onCreate list: " + SelectionList.getInstance());

        getSupportActionBar().setTitle(listName);
        toDoItemList = SelectionList.getInstance().getToDoList(listName);

        ToDoItem toDoEntry = new ToDoItem("Test", "Nothing", false, false );
        toDoItemList.addItem(toDoEntry);
        toDoItemList.addItem( new ToDoItem("Test2", "Nothing", false, false ) );

        Log.d(MainActivity.getTAG(), "onCreate: "+ toDoItemList);

        // adapter is saved so that we can tell it to update it's data later.
        adapter = new ToDoListRowAdapter(this, R.layout.todo_item_row_layout, toDoItemList);
        final ListView listView = (ListView) findViewById(R.id.toDoListView);

        listView.setAdapter(adapter);


        /**
         * set up short press detector to launch viewer
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(MainActivity.getTAG(), "onItemClick(" + i + ")");
                Intent nextActivity = new Intent(ToDoListActivity.this, ViewToDoItemActivity.class);
                // pass viewer the listname and index
                nextActivity.putExtra("ToDoItemIndex", i);
                nextActivity.putExtra("ToDoListName", toDoItemList.getListName());
                state = listView.onSaveInstanceState();
                startActivity(nextActivity);
            }
        });

        /**
         *  set up long press detector to launch editor
         */

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(MainActivity.getTAG(), "onItemClick(" + i + ")");
                Intent nextActivity = new Intent(ToDoListActivity.this, ToDoItemEditorActivity.class);
                // pass editor the listname and index
                nextActivity.putExtra("ToDoItemIndex", i);
                nextActivity.putExtra("ToDoListName", toDoItemList.getListName());
                state = listView.onSaveInstanceState();
                startActivity(nextActivity);
                return true;
            }

        });

    }

    /**
     *  Overridden to restore listview status, so that we return to same position after editing/viewing item
     *  also notifies list to updatedata incase it has changed. Not ideal as always called whether data changed
     *  or not, but keeps things simple.
     */

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = (ListView) findViewById(R.id.toDoListView);
        getAdapter().notifyDataSetChanged();  // request listview updates it's content data incase it has been edited before resuming
        if (state != null) {  // if we have a saved state, then restore it
     //       listView.onRestoreInstanceState(state);
            Log.d(MainActivity.getTAG(), "onResume: restore list status");}


    }
}
