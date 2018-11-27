package fi.metropolia.foobar.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class ToDoItemEditorActivity extends AppCompatActivity {
    ToDoItem dummy = new ToDoItem("Test item", "Information", true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item_editor);
    }

    public void onAddClick(View v) {
        Switch highlight = (Switch) findViewById(R.id.highlight);
        Switch done = (Switch) findViewById(R.id.done);

        Boolean isHighlighted = highlight.isChecked();
        Boolean isDone = done.isChecked();

        if (isDone){

        }


    }
}
