package fi.metropolia.foobar.todo;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {

    ToDoItemList toDoItemList;

    public ToDoListRowAdapter getAdapter() {
        return adapter;
    }

    private ToDoListRowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        //create dummy item
        ToDoItem toDoEntry = new ToDoItem("Test", "Nothing", false, false );

       toDoItemList = new ToDoItemList("testlist", this);
       toDoItemList.addItem(toDoEntry);

        toDoItemList.addItem( new ToDoItem("Test2", "Nothing", false, false ) );

        Log.d(MainActivity.getTAG(), "onCreate: "+ toDoItemList);

       adapter = new ToDoListRowAdapter(this, R.layout.todo_item_row_layout, toDoItemList);
       final ListView listView = (ListView) findViewById(R.id.toDoListView);

       listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(MainActivity.getTAG(), "onItemClick(" + i + ")");
                Intent nextActivity = new Intent(ToDoListActivity.this, ViewToDoItemActivity.class);
                nextActivity.putExtra("ToDoItemIndex", i);
                nextActivity.putExtra("ToDoListName", toDoItemList.getListName());
                Parcelable state = listView.onSaveInstanceState();
                startActivity(nextActivity);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(MainActivity.getTAG(), "onItemClick(" + i + ")");
                Intent nextActivity = new Intent(ToDoListActivity.this, ToDoItemEditorActivity.class);
                nextActivity.putExtra("ToDoItemIndex", i);
                nextActivity.putExtra("ToDoListName", toDoItemList.getListName());
                Parcelable state = listView.onSaveInstanceState();
                startActivity(nextActivity);
                return true;
            }


        });



    }
}
