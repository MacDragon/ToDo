package fi.metropolia.foobar.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class ViewToDoItemActivity extends AppCompatActivity {
    ToDoItem dummy = new ToDoItem("Dummy Test Item", "Testing\nmultiple\nlines", true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_to_do_item);
        getSupportActionBar().setTitle(dummy.getTitle());
        ((TextView)findViewById(R.id.showDesc)).setText(dummy.getDescription());
        ((Switch)findViewById(R.id.showHighlight)).setChecked(dummy.isHighlight());
        ((Switch)findViewById(R.id.showDone)).setChecked(dummy.isDone());
    }

    @Override
    public void finish() {
        super.finish();
        dummy.setHighlight(((Switch)findViewById(R.id.showHighlight)).isChecked());
        dummy.setDone(((Switch)findViewById(R.id.showDone)).isChecked());
    }
}
