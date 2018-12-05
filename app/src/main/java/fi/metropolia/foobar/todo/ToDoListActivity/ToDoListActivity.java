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
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import fi.metropolia.foobar.todo.*;


/**
 * Activity to show a todolist called from main selector activity
 */

public class ToDoListActivity extends TransitionActivity implements DragListener {

    private ToDoItemList toDoItemList;
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

    public void listRename(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename list");
        final Context renameContext = this;

        // get a view from activity in order to use inflator so that we can use a dialog resource file to create a nicer formatted dialog easily
        // as Menuitem does not seem to have this available.

        final View inflatedView = (View)findViewById(R.id.toDoListView).inflate(renameContext,R.layout.dialog_rename_list, null);

        // link edittext to local object.

        final EditText input = (EditText) inflatedView.findViewById(R.id.listName);

        input.setText(toDoItemList.getListName());
        builder.setView(inflatedView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // try to rename list
                 if (!toDoItemList.saveList( input.getText().toString())){
                     // rename failed, assume file exists, give error.
                     final View viewHandle = (View)findViewById(R.id.toDoListView).inflate(renameContext,R.layout.dialog_rename_list, null);
                     AlertDialog.Builder titleMissing = new AlertDialog.Builder(renameContext);
                     titleMissing.setTitle("Rename failed");
                     titleMissing.setMessage("List name already exists?");
                     titleMissing.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                         }
                     });
                     titleMissing.show();

                 } else {
                     getSupportActionBar().setTitle(toDoItemList.getListName());
                 }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void listDelete(MenuItem item){

        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this);
        confirmDelete.setTitle("Delete item");
        confirmDelete.setMessage("Are you sure you want to delete this list?");
        confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            /*    toDoItemList.deleteList(); */
                SelectionList.getInstance().deleteList(toDoItemList.getListName());
                finish();
            }
        });
        confirmDelete.setNegativeButton("Cancel", null).show();


    }

    /**
     * Returns the listrow adapter saved earlier so it can be used to request data refresh
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

        Log.d(MainActivity.getTAG(), "Entering listview");

        // specifity the activities layout file
        setContentView(R.layout.activity_to_do_list);

        //create dummy list and items

        // retrieve the extras information in order to get the passed listname to display
        Bundle extras = getIntent().getExtras();
        // extras failing when returning from editor - worked around by using launchmode singletop
        String listName = extras.getString("listName");

        //set the 'title bar' to the listName, so that we know what list we are displaying.
        getSupportActionBar().setTitle(listName);

        // get the actual list object so we can work with it.
        toDoItemList = SelectionList.getInstance().getToDoList(listName);
        Log.d(MainActivity.getTAG(), "onCreate: " + toDoItemList.getToDoListArray());

        // handle to adapter is saved so that we can tell it to update data later from onResume..
        adapter = new ToDoListRowAdapter(this, R.layout.todo_item_row_layout, toDoItemList, this);

        listView = (RecyclerView)findViewById(R.id.toDoListView);


        DefaultItemAnimator animator = new DefaultItemAnimator();
        listView.setItemAnimator(animator);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        listView.setLayoutManager(layoutManager);

     //   listView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter, toDoItemList);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(listView);

        listView.setAdapter(adapter);
    }

    /**
     *  Overridden to restore listview status, so that we return to same position after editing/viewing item
     *  also notifies listview to update it's contained data incase it has changed during editing.
     *  Not ideal as always called whether data changed or not, but keeps things simple.
     */

    @Override
    protected void onResume() {
        super.onResume();
        // request listview updates it's content data incase it has been edited before resuming
        getAdapter().notifyDataSetChanged(); // doesn't seem to be needed now launchmode singletop
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(MainActivity.getTAG(), "onPause: ");
        SharedPreferences prefPut = getSharedPreferences("Settings", Activity.MODE_PRIVATE); // move tag to mainactivity
        SharedPreferences.Editor prefEditor = prefPut.edit();
        toDoItemList.saveList();
        if (!toDoItemList.isDeleted()){
            prefEditor.putString("lastList", toDoItemList.getListName());
        } else {
            prefEditor.putString("lastList","");
        }

        prefEditor.commit();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

}
