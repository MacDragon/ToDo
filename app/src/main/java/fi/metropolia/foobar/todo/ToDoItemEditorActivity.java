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

    /**
     * Gets the selected list, item and items index.
     * Checks if user is adding new item or editing old one.
     * @param savedInstanceState
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
        ((NumberPicker)findViewById(R.id.indexPicker)).setMinValue(1);
        //Check if user is creating a new item
        if (i == -1) {
            //Hide unnecessary widgets when creating new item
            ((Button)findViewById(R.id.delete)).setVisibility(View.INVISIBLE);
            ((Switch)findViewById(R.id.done)).setVisibility(View.INVISIBLE);
            item = new ToDoItem("", "", false);
            ((NumberPicker)findViewById(R.id.indexPicker)).setMaxValue(list.getToDoListArray().size() + 1);
            ((NumberPicker)findViewById(R.id.indexPicker)).setValue(list.getToDoListArray().size() +1);
            ((Button)findViewById(R.id.edit)).setText("Add");
            getSupportActionBar().setTitle("Add");
        } else {
            //Do this if user is editing item
            item = list.getToDoItem(i);
            ((NumberPicker)findViewById(R.id.indexPicker)).setMaxValue(list.getToDoListArray().size());
            ((NumberPicker)findViewById(R.id.indexPicker)).setValue(i + 1);
            ((Button)findViewById(R.id.edit)).setText("Save");
            getSupportActionBar().setTitle(item.getTitle());
        }
        ((TextView)findViewById(R.id.editTitle)).setText(item.getTitle());
        ((TextView)findViewById(R.id.editDesc)).setText(item.getDescription());
        ((Switch)findViewById(R.id.highlight)).setChecked(item.isHighlight());
        ((Switch)findViewById(R.id.done)).setChecked(item.isDone());
    }

    /**
     * Method to add/save the item to the list.
     * Also checks if the item is missing a title and denies the edit
     */

    public void onAddClick(View v) {
        if (((TextView)findViewById(R.id.editTitle)).getText().toString().isEmpty()) {
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
            item.setTitle(((TextView)findViewById(R.id.editTitle)).getText().toString());
            item.setDescription(((TextView)findViewById(R.id.editDesc)).getText().toString());
            item.setHighlight(((Switch)findViewById(R.id.highlight)).isChecked());
            item.setDone(((Switch)findViewById(R.id.done)).isChecked());
            if (i != -1) {
                list.getToDoListArray().remove(i);
            }
            list.getToDoListArray().add(((NumberPicker)findViewById(R.id.indexPicker)).getValue() - 1, item);

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
