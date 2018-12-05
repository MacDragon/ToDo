package fi.metropolia.foobar.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Set;

public class ToDoItemEditorActivity extends TransitionActivity {
    ToDoItem item; // item
    ToDoItemList list; // list
    int i; // item index
    TextView titleView; // title TextView
    TextView descView; // description TextView
    Switch highlightSwitch; // highlight Switch
    Switch doneSwitch; // done Switch
    NumberPicker picker; // Item position selector
    Button editButton; // Save button
    Button deleteButton; // Delete button

    /**
     * Gets the selected list, item and its index.
     * Check if user is adding new item or editing one.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item_editor);
        //Get the list and selected item from extras
        Bundle extras = getIntent().getExtras();
        String listName = extras.getString("ToDoListName");
        i = extras.getInt("ToDoItemIndex");
        list = SelectionList.getInstance().getToDoList(listName);

        // Specify widgets
        titleView = (TextView)findViewById(R.id.editTitle);
        descView = (TextView)findViewById(R.id.editDesc);
        highlightSwitch = (Switch)findViewById(R.id.highlight);
        doneSwitch = (Switch)findViewById(R.id.done);
        picker = (NumberPicker)findViewById(R.id.indexPicker);
        editButton = (Button)findViewById(R.id.edit);
        deleteButton = (Button)findViewById(R.id.delete);
        picker.setMinValue(1);

        //Check if user is creating a new item
        if (i == -1) {
            deleteButton.setVisibility(View.INVISIBLE); // Hide delete button
            doneSwitch.setVisibility(View.INVISIBLE); // Hide done switch
            item = new ToDoItem("", "", false); // Create empty ToDoItem
            picker.setMaxValue(list.size() + 1); // Set indexPickers max value to one bigger than list size
            picker.setValue(list.size() +1); // Set selected value to last number
            editButton.setText("Add"); // Change edit button to say "Add"
            getSupportActionBar().setTitle("Add"); // Set actionbar to "Add"
        } else {
            //Do this if user is editing item
            item = list.getToDoItem(i); // Get selected item
            picker.setMaxValue(list.size()); //Set pickers max value to list size
            picker.setValue(i + 1); //Set pickers current value to selected item
            editButton.setText("Save"); // Set edit button to say "Edit"
            getSupportActionBar().setTitle(item.getTitle()); // Set Actionbar to items title
        }
        // Set items values to widgets
        titleView.setText(item.getTitle());
        descView.setText(item.getDescription());
        highlightSwitch.setChecked(item.isHighlight());
        doneSwitch.setChecked(item.isDone());
    }

    /**
     * Method to add/save the item to the list.
     * Opens dialog box if title is not acceptable
     */

    public void onAddClick(View v) {
        if (titleView.getText().toString().isEmpty()) {
            // If title is empty open a dialog and don't save
            AlertDialog.Builder titleMissing = new AlertDialog.Builder(this); // Creating alertDialog
            titleMissing.setTitle("Title missing!"); // Set alert dialogs title
            titleMissing.setMessage("ToDo item must have a title to be valid."); // Set dialog's message
            // Adding button to dialog box
            titleMissing.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            titleMissing.show(); //Show dialog box
            Log.d("ToDo", "Failed");
        } else if(list.itemExists(titleView.getText().toString()) && !(item.getTitle().equals(titleView.getText().toString()))) {
            // If title already exists in this list open a dialog and don't save
            AlertDialog.Builder itemExists = new AlertDialog.Builder(this); // Creating alertDialog
            itemExists.setTitle("Item already exists!"); // Set alert dialogs title
            itemExists.setMessage("This list already contains an item with this name."); // Set dialog's message
            // Adding button to dialog box
            itemExists.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            itemExists.show(); //Show dialog box
        } else {
            // If everything is fine, save the changes
            // Set new values to selected item
            item.setTitle(titleView.getText().toString());
            item.setDescription(descView.getText().toString());
            item.setHighlight(highlightSwitch.isChecked());
            item.setDone(doneSwitch.isChecked());
            // Do this if editing item
            if (i != -1) {
                list.remove(i);
            }
            list.add(picker.getValue() - 1, item); // Move item to right position

            Log.d("ToDo", item.toString());

            finish(); // End the activity
        }
    }

    /**
     * Method to delete an item from a list and asking confirmation from the user.
     *
     */

    public void deleteItem(View v) {
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this);
        confirmDelete.setTitle("Delete item"); // Dialog title
        confirmDelete.setMessage("Are you sure you want to delete this item?"); // Dialog message
        //Confirm delete
        confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.remove(i);
                finish();
            }
        });
        confirmDelete.setNegativeButton("Cancel", null).show(); // Cancel delete
    }
}
