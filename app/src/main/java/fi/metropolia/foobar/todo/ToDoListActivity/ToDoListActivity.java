package fi.metropolia.foobar.todo.ToDoListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.*;
import android.widget.EditText;
import fi.metropolia.foobar.todo.*;

/**
 * Activity to show a todolist called from main selector activity
 * extends Transition activity instead of AppcompatActivity to automatically get transition animations defined there
 * See package-info.java for further credit in implementation
 */
public class ToDoListActivity extends TransitionActivity implements DragListener {

    private ToDoItemList list;
    private ToDoListRowAdapter adapter;
    private RecyclerView listView;
    private ItemTouchHelper touchHelper;

    // https://developer.android.com/guide/topics/ui/dialogs#CustomLayout how to implement a dialog in android

    /**
     *  load options menu layout to add rename to actionbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_list_menu, menu);
        return true;
    }

    /**
     * onClick event for rename menu item, to request changed list name and resave it.
     * @param item calling menu item to satisfy calling style.
     */
    public void listRename(MenuItem item){
        // create dialog to request new listname.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename list");
        // save the context so it can be reused inside second error dialog
        final Context renameContext = this;

        // get a view from activity in order to use inflator so that we can use a dialog resource file to create a nicer formatted dialog easily
        // as Menuitem does not seem to have this available.

        final View inflatedView = (View)findViewById(R.id.toDoListView).inflate(renameContext,R.layout.dialog_rename_list, null);

        // link edittext to local object to manipulate string from it.
        final EditText input = (EditText) inflatedView.findViewById(R.id.listName);
        input.requestFocus();

 //      showKeyboard();

        // set dialogs EditText to current list name.
        input.setText(list.getListName());
        builder.setView(inflatedView);
        // setup button for accepting modified listname and actioning the list rename, if possible.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // try to rename list
                 if (!list.saveList( input.getText().toString())){
   //                  closeKeyboard();
                     // saving to new name failed, assume because file exists, give error.
                     AlertDialog.Builder titleMissing = new AlertDialog.Builder(renameContext);
                     titleMissing.setTitle("Rename failed");
                     titleMissing.setMessage("List name already exists?");
                     // dialog only needs to have a single acknowledgement button, it's only informing
                     // the action cannot be done and needs to do no further works.
                     titleMissing.setNegativeButton("Ok", null );
                     titleMissing.show();

                 } else { // renaming list succeeded
                     // set the actionBar's title to the newly renamed list title.
                     getSupportActionBar().setTitle(list.getListName());
                     // closeKeyboard();
                 }
            }
        });

        // negative button doesn't need to do anything, dialog closes automatically.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
    //            closeKeyboard();
                dialog.cancel();
            }
        });
        builder.show();

    }

    /**
     * method to delete the current active list
     * @param item as this is called from menu the calling item is passed and used for context.
     */
    public void listDelete(MenuItem item){
        // create confirmation dialog to ensure list deletion is not a mistake.
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this);
        confirmDelete.setTitle("Delete item");
        confirmDelete.setMessage("Are you sure you want to delete this list?");
        confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // tell the global object to delete the current list.
                SelectionList.getInstance().deleteList(list.getListName());
                // finish the current activity, as with no list to work on there is nothing to do here.
                finish();
            }
        });
        // negative button doesn't need to do anything, dialog closes automatically.
        confirmDelete.setNegativeButton("Cancel", null);
        confirmDelete.show();

    }

    /**
     * Returns the listrow adapter saved earlier so it can be used to request data refresh on resume.
     *
     * @return returns the handle to RecyclerView's adapter
     */
    public ToDoListRowAdapter getAdapter() {
        return adapter;
    }


    /**
     * Handler to respond to floating addItem button to call edit activity with request to create a new item
     * using -1 as index to indicate that we are not editing existing item.
     *
     * @param view calling view to satisfy calling style.
     */
    public void addClick(View view){
        // create the intent object to call editor activity
        Intent nextActivity = new Intent(ToDoListActivity.this, ToDoItemEditorActivity.class);
        // set extras informatino to specify the list name so editor can use right list
        // also pass -1 to indicate we are creating a new item for the list, as this is an index that can not exist
        nextActivity.putExtra("ToDoItemIndex", -1);
        nextActivity.putExtra("ToDoListName", list.getListName());
        // start the add item activity
        startActivity(nextActivity);
    }

    /**
     * Setup ToDoList when activity called, using extra info to load the list from global object using list name.
     *
     * @param savedInstanceState Previously saved state instance data to restore status.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // specifity the activities layout file
        setContentView(R.layout.activity_to_do_list);

        // retrieve the extras information in order to get the passed listname to display
        Bundle extras = getIntent().getExtras();
        // extras failing when returning from editor - worked around by using launchmode singletop
        String listName = extras.getString("listName");

        //set the 'title bar' to the listName, so that we know what list we are displaying.
        getSupportActionBar().setTitle(listName);

        // get the actual list object so we can work with it.
        list = SelectionList.getInstance().getToDoList(listName);

        // handle to adapter is saved so that we can tell it to update data later from onResume..
        adapter = new ToDoListRowAdapter(this, R.layout.todo_item_row_layout, list, this);

        // get handle to the RecyclerView in order to setup it's touch events, adapter etc.
        listView = (RecyclerView)findViewById(R.id.toDoListView);

        // attach the default android item animations to the RecyclerView to react to drag/swipe events.
        listView.setItemAnimator(new DefaultItemAnimator());

        // tell the RecyclerView that we are using a normal vertical row layout.
        listView.setLayoutManager(new LinearLayoutManager(this));

        // tell the RecyclerView that it's size will not change depending on it's contents, this improves performance
        listView.setHasFixedSize(true);

        // setup the touch responder for drag/swipe events in the RecyclerView.
        touchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter, list));
        touchHelper.attachToRecyclerView(listView);

        // finally setup the RecyclerView's adapter so it knows what to display.
        listView.setAdapter(adapter);
        if ( list.size() == 0 ) {
            addClick(listView);
        }
    }

    /**
     *  overridden onResume to notify RecyclerView to update it's contained data that may have been changed during editing.
     *  Not ideal as always called whether data changed or not, but keeps things simple.
     */
    @Override
    protected void onResume() {
        super.onResume();
        getAdapter().notifyDataSetChanged(); // doesn't seem to be needed now launchmode singletop
    }

    /**
     * override default onPause method in order to store current list in preferences and save list data.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // get handle to the application preferences data.
        SharedPreferences prefPut = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = prefPut.edit();
        // ensure the current list data is stored to permanent storage as pause may mean activity is closing.
        list.saveList();
        if (list.listFileExists()){
            // store the current listname in preferences to allow resuming directly into it.
            prefEditor.putString("lastList", list.getListName());
        } else {
            // remove last saved list if the listfile we are pausing does not actually exist anymore
            // as this means the list has been deleted
            prefEditor.putString("lastList","");
        }
        // store the preferences
        prefEditor.commit();
    }

    /**
     * method to allow the activity to pass drag event back from recycle view into touch helper as a callback.
     * @param viewHolder handle to the current viewHolder storing the RecyclerView's row.
     */
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

}
