package fi.metropolia.foobar.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

public class ViewToDoItemActivity extends AppCompatActivity {
    ToDoItem item;
    ToDoItemList list;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_to_do_item);
        Bundle extras = getIntent().getExtras();
        String listName = extras.getString("ToDoListName");
        i = extras.getInt("ToDoItemIndex");
        list = SelectionList.getInstance().getToDoList(listName);
        item = list.getToDoItem(i);

        getSupportActionBar().setTitle(item.getTitle());
        ((TextView)findViewById(R.id.showDesc)).setText(item.getDescription());
        ((Switch)findViewById(R.id.showHighlight)).setChecked(item.isHighlight());
        ((Switch)findViewById(R.id.showDone)).setChecked(item.isDone());
    }

    /**
     * When exiting the activity save the changes made
     */
    @Override
    public void onPause() {
        item.setHighlight(((Switch)findViewById(R.id.showHighlight)).isChecked());
        item.setDone(((Switch)findViewById(R.id.showDone)).isChecked());
        Log.d("ToDo", "finish: test");
        super.onPause();
    }
}
