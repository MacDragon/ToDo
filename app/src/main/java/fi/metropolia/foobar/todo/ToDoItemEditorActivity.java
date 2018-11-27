package fi.metropolia.foobar.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ToDoItemEditorActivity extends AppCompatActivity {
    ToDoItem dummy = new ToDoItem("Test item", "Information", true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item_editor);
        ((TextView)findViewById(R.id.editTitle)).setText(dummy.getTitle());
        ((TextView)findViewById(R.id.editDesc)).setText(dummy.getDescription());
        ((Switch)findViewById(R.id.highlight)).setChecked(dummy.isHighlight());
        ((Switch)findViewById(R.id.done)).setChecked(dummy.isDone());
    }

    public void onAddClick(View v) {
        Switch highlight = (Switch) findViewById(R.id.highlight);
        Switch done = (Switch) findViewById(R.id.done);

        Boolean isHighlighted = highlight.isChecked();
        Boolean isDone = done.isChecked();

        dummy.setTitle(((TextView)findViewById(R.id.editTitle)).getText().toString());
        dummy.setDescription(((TextView)findViewById(R.id.editDesc)).getText().toString());
        dummy.setDone(isDone);
        dummy.setHighlight(isHighlighted);

    }
}
