package fi.metropolia.foobar.todo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ToDoItemEditorActivity extends AppCompatActivity {
    ToDoItem dummy = new ToDoItem("Test item", "Information", true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item_editor);
        getSupportActionBar().setTitle(dummy.getTitle());
        ((TextView)findViewById(R.id.editTitle)).setText(dummy.getTitle());
        ((TextView)findViewById(R.id.editDesc)).setText(dummy.getDescription());
        ((Switch)findViewById(R.id.highlight)).setChecked(dummy.isHighlight());
        ((Switch)findViewById(R.id.done)).setChecked(dummy.isDone());
        ((NumberPicker)findViewById(R.id.indexPicker)).setMinValue(1);
        ((NumberPicker)findViewById(R.id.indexPicker)).setMaxValue(12);
        ((NumberPicker)findViewById(R.id.indexPicker)).setValue(3);



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
            Switch highlight = (Switch)
                    findViewById(R.id.highlight); // switch casting might be redundant
            Switch done = (Switch) findViewById(R.id.done);

            Boolean isHighlighted = highlight.isChecked();
            Boolean isDone = done.isChecked();

            dummy.setTitle(((TextView) findViewById(R.id.editTitle)).getText().toString());
            dummy.setDescription(((TextView) findViewById(R.id.editDesc)).getText().toString());
            dummy.setDone(isDone);
            dummy.setHighlight(isHighlighted);
            Log.d("ToDo", dummy.toString());

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
                finish();
            }
        });
        confirmDelete.setNegativeButton("Cancel", null).show();
    }
}
