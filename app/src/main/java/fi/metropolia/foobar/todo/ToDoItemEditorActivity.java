package fi.metropolia.foobar.todo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Set;

/**
 * Activity class for editing and adding new items to a list
 */
public class ToDoItemEditorActivity extends TransitionActivity {
    private ToDoItem item; // item
    private ToDoItemList list; // list
    private int i; // item index
    private TextView titleView; // title TextView
    private TextView descView; // description TextView
    private Switch highlightSwitch; // highlight Switch
    private Switch doneSwitch; // done Switch
    private NumberPicker picker; // Item position selector
    private Button editButton; // Save button
    private Button deleteButton; // Delete button

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
        titleView = findViewById(R.id.editTitle);
        descView = findViewById(R.id.editDesc);
        highlightSwitch = findViewById(R.id.highlight);
        doneSwitch = findViewById(R.id.done);
        picker = findViewById(R.id.indexPicker);
        editButton = findViewById(R.id.edit);
        deleteButton = findViewById(R.id.delete);
        picker.setMinValue(1); // Set the lowest value for index picker

        //Check if user is creating a new item
        if (i == -1) {
            deleteButton.setVisibility(View.INVISIBLE); // Hide delete button
            doneSwitch.setVisibility(View.INVISIBLE); // Hide done switch
            item = new ToDoItem("", "", false); // Create empty ToDoItem
            picker.setMaxValue(list.size() + 1); // Set indexPickers max value to one bigger than list size
            picker.setValue(list.size() +1); // Set selected value to last number
            editButton.setText("Add"); // Change edit button to say "Add"
            getSupportActionBar().setTitle("Add item"); // Set actionbar to "Add"
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
        titleView.requestFocus();
    }

    /**
     * Method to add/save the item to the list.
     * Opens dialog box if title is not acceptable
     *
     * @param view view of calling item, not needed except to satisfy calling type.
     */
    public void onAddClick(View view) {
        if (titleView.getText().toString().isEmpty()) {
            // If title is empty open a dialog and don't save
            AlertDialog.Builder titleMissing = new AlertDialog.Builder(this); // Create dialog
            titleMissing.setTitle("Title missing!"); // Set dialog title
            titleMissing.setMessage("ToDo item must have a title to be valid."); // Set dialog message
            // Adding button to dialog box
            titleMissing.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            titleMissing.show(); //Show dialog box
        // Checks if item with given name already exists in this list and
        // check if the name wasn't changed
        } else if(list.itemExists(titleView.getText().toString()) && !(item.getTitle().equals(titleView.getText().toString()))) {
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
                list.add(picker.getValue() - 1, item);
                finish();
            // When adding new items add them to the list and reset the views
            } else {

                // Notify user that item has been added to the list
                Toast toast = Toast.makeText(this, "Item added", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                toast.show();
                list.add(picker.getValue() - 1, item); // Move item to right position
                resetViews();
            }
        }
    }

    /**
     * Method to delete an item from a list and asking confirmation from the user.
     *
     * @param view  view of calling item, not needed except to satisfy calling type.
     */
    public void deleteItem(View view) {
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this); // Create new dialog
        confirmDelete.setTitle("Delete item"); // Set dialog title
        confirmDelete.setMessage("Are you sure you want to delete this item?"); // Set dialog message
        // Create yes and cancel buttons
        confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.remove(i);
                finish();
            }
        });
        confirmDelete.setNegativeButton("Cancel", null).show(); // Cancel delete
    }

    /**
     * Method to clear views so that multiple items can be added in a row without closing the activity
     */
    public void resetViews() {
        item = new ToDoItem("", "", false); // Reset the item variable
        picker.setMaxValue(list.size() + 1); // Set indexPickers max value to one bigger than list size
        picker.setValue(list.size() +1); // Set selected value to last number
        // Set items values to widgets
        titleView.setText(item.getTitle());
        descView.setText(item.getDescription());
        highlightSwitch.setChecked(item.isHighlight());
        doneSwitch.setChecked(item.isDone());
        titleView.requestFocus();
    }
}
