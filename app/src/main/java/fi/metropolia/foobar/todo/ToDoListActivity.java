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

    private ToDoItemList toDoItemList;
    private ToDoListRowAdapter adapter;

  //  private Parcelable state = null;

    /**
     * Returns the listrow adapter saved earlier for data refresh
     *
     * @return
     */

    public ToDoListRowAdapter getAdapter() {
        return adapter;
    }


    /**
     * Handler to respond to floating addItem button to call edit activity with request to create a new item
     * using -1 as index to indicate that we are not editing existing item.
     *
     * @param view
     */

    public void addClick(View view){
        // create the intent object to call editor activity
        Intent nextActivity = new Intent(ToDoListActivity.this, ToDoItemEditorActivity.class);
        // set extras informatino to specify the list name so editor can use right list
        // also pass -1 to indicate we are creating an item
        nextActivity.putExtra("ToDoItemIndex", -1);
        nextActivity.putExtra("ToDoListName", toDoItemList.getListName());
        // retrieve the listview handle so that we can save it's state
    //    final ListView listView = (ListView) findViewById(R.id.toDoListView);
        // store the current state of listview, so that it can be restored after
    //    state = listView.onSaveInstanceState();
        // activity is expecting to be returned to, so start as such.
        //startActivityForResult(nextActivity);
        startActivity(nextActivity);
    }

    /**
     * Setup ToDoList when activity called, using extra info to read list name.
     *
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // specifity the activities layout file
        setContentView(R.layout.activity_to_do_list);

        //create dummy list and items

        // retrieve the extras information in order to get the passed listname to display
        Bundle extras = getIntent().getExtras();
        // extras failing when returning from editor - worked around by using launchmode singletop
        String listName = extras.getString("listName");

      //  Log.d(MainActivity.getTAG(), "onCreate: " + listName);
      //  Log.d(MainActivity.getTAG(), "onCreate list: " + SelectionList.getInstance());

        //set the 'title bar' to the listName, so that we know what list we are displaying.
        getSupportActionBar().setTitle(listName);

        // get the actual list object so we can work with it.
        toDoItemList = SelectionList.getInstance().getToDoList(listName);

        // temporary dummy items to allow quick testing, till saving and loading is working, no need to save the variable locally
        toDoItemList.addItem(new ToDoItem("Test", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test2", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test3", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test4", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test5", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test6", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test7", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test8", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test9", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test10", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test11", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test12", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test13", "Nothing", false, false));
        toDoItemList.addItem(new ToDoItem("Test14", "Nothing", false, false));


    //    Log.d(MainActivity.getTAG(), "onCreate: " + toDoItemList);

        // handle to adapter is saved so that we can tell it to update data later from onResume..
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
          //      state = listView.onSaveInstanceState();
                // activity is expecting to be returned to, so start as such.
                //startActivityForResult(nextActivity);
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
         //       state = listView.onSaveInstanceState();
                startActivity(nextActivity);
                return true;
            }

        });

    }

    /**
     *  Overridden to restore listview status, so that we return to same position after editing/viewing item
     *  also notifies listview to update it's contained data incase it has changed during editing.
     *  Not ideal as always called whether data changed or not, but keeps things simple.
     */

    @Override
    protected void onResume() {
        super.onResume();
        // retrieve the ListView object
        ListView listView = (ListView) findViewById(R.id.toDoListView);
        // request listview updates it's content data incase it has been edited before resuming
        getAdapter().notifyDataSetChanged(); // doesn't seem to be needed now launchmode singletop
      //  if (state != null) {  // if we have a saved state, then restore it
    //        listView.onRestoreInstanceState(state); // no longer needed due to launchmode singletop
      //      Log.d(MainActivity.getTAG(), "onResume: restore list status");
       // }

    }


}
