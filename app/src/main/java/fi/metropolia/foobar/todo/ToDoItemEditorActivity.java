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

public class ToDoItemEditorActivity extends AppCompatActivity {
    ToDoItem item;
    ToDoItemList list;
    int i;
    TextView titleView = findViewById(R.id.editTitle);
    TextView descView = findViewById(R.id.editDesc);
    Switch highlightSwitch = (Switch)findViewById(R.id.highlight);
    Switch doneSwitch = (Switch)findViewById(R.id.done);
    NumberPicker picker = (NumberPicker)findViewById(R.id.indexPicker);
    Button editButton = findViewById(R.id.edit);
    Button deleteButton = findViewById(R.id.delete);

    /**
     * Gets the selected list, item and items index.
     * Checks if user is adding new item or editing old one.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item_editor);
        //Get the list and selected item
        Bundle extras = getIntent().getExtras();
        String listName = extras.getString("ToDoListName");
        i = extras.getInt("ToDoItemIndex");
        list = SelectionList.getInstance().getToDoList(listName);
        picker.setMinValue(1);
        //Check if user is creating a new item
        if (i == -1) {
            deleteButton.setVisibility(View.INVISIBLE); // Hide delete button
            doneSwitch.setVisibility(View.INVISIBLE); // Hide done switch
            item = new ToDoItem("", "", false); // Create empty ToDoItem
            picker.setMaxValue(list.getToDoListArray().size() + 1);
            picker.setValue(list.getToDoListArray().size() +1);
            editButton.setText("Add");
            getSupportActionBar().setTitle("Add");
        } else {
            //Do this if user is editing item
            item = list.getToDoItem(i);
            picker.setMaxValue(list.getToDoListArray().size());
            picker.setValue(i + 1);
            ((Button)findViewById(R.id.edit)).setText("Save");
            getSupportActionBar().setTitle(item.getTitle());
        }
        titleView.setText(item.getTitle());
        descView.setText(item.getDescription());
        highlightSwitch.setChecked(item.isHighlight());
        doneSwitch.setChecked(item.isDone());
    }

    /**
     * Method to add/save the item to the list.
     * Check if the new title is empty --> open dialog box and don't save.
     */

    public void onAddClick(View v) {
        if (titleView.getText().toString().isEmpty()) {
            AlertDialog.Builder titleMissing = new AlertDialog.Builder(this);
            titleMissing.setTitle("Title missing!");
            titleMissing.setMessage("ToDo item must have a title to be valid.");
            titleMissing.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            titleMissing.show();
            Log.d("ToDo", "Failed");
        } else {
            item.setTitle(titleView.getText().toString());
            item.setDescription(descView.getText().toString());
            item.setHighlight(highlightSwitch.isChecked());
            item.setDone(doneSwitch.isChecked());
            if (i != -1) {
                list.getToDoListArray().remove(i);
            }
            list.getToDoListArray().add(picker.getValue() - 1, item);

            Log.d("ToDo", item.toString());

            finish();
        }
    }

    /**
     * Method to delete an item from a list and asking confirmation from the user.
     *
     */

    public void deleteItem(View v) {
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this);
        confirmDelete.setTitle("Delete item");
        confirmDelete.setMessage("Are you sure you want to delete this item?");
        confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.getToDoListArray().remove(i);
                finish();
            }
        });
        confirmDelete.setNegativeButton("Cancel", null).show();
    }
}
