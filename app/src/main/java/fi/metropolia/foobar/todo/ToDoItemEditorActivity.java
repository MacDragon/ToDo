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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item_editor);

        Bundle extras = getIntent().getExtras();
        String listName = extras.getString("ToDoListName");
        i = extras.getInt("ToDoItemIndex");
        list = SelectionList.getInstance().getToDoList(listName);
        ((NumberPicker)findViewById(R.id.indexPicker)).setMinValue(1);

        if (i == -1) {
            ((Button)findViewById(R.id.delete)).setVisibility(View.INVISIBLE);
            ((Switch)findViewById(R.id.done)).setVisibility(View.INVISIBLE);
            item = new ToDoItem("", "", false);
            ((NumberPicker)findViewById(R.id.indexPicker)).setMaxValue(list.getToDoList().size() + 1);
            ((NumberPicker)findViewById(R.id.indexPicker)).setValue(list.getToDoList().size());
        } else {
            item = list.getToDoItem(i);
            ((NumberPicker)findViewById(R.id.indexPicker)).setMaxValue(list.getToDoList().size());
            ((NumberPicker)findViewById(R.id.indexPicker)).setValue(i + 1);
        }
        getSupportActionBar().setTitle(item.getTitle());
        ((TextView)findViewById(R.id.editTitle)).setText(item.getTitle());
        ((TextView)findViewById(R.id.editDesc)).setText(item.getDescription());
        ((Switch)findViewById(R.id.highlight)).setChecked(item.isHighlight());
        ((Switch)findViewById(R.id.done)).setChecked(item.isDone());

        ((Button)findViewById(R.id.edit)).setText("Edit");

    }

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
            Switch highlight = (Switch) findViewById(R.id.highlight);
            Switch done = (Switch) findViewById(R.id.done);

            item.setTitle(((TextView)findViewById(R.id.editTitle)).getText().toString());
            item.setDescription(((TextView)findViewById(R.id.editDesc)).getText().toString());
            item.setHighlight(((Switch)findViewById(R.id.highlight)).isChecked());
            item.setDone(((Switch)findViewById(R.id.done)).isChecked());

            list.getToDoList().remove(i);
            list.getToDoList().add(((NumberPicker)findViewById(R.id.indexPicker)).getValue() - 1, item);

            Log.d("ToDo", item.toString());

            finish();
        }
    }

    public void deleteItem(View v) {
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this);
        confirmDelete.setTitle("Delete item");
        confirmDelete.setMessage("Are you sure you want to delete this item?");
        confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.getToDoList().remove(i);
                finish();
            }
        });
        confirmDelete.setNegativeButton("Cancel", null).show();
    }
}
