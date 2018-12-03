package fi.metropolia.foobar.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    public static String getTAG() {
        return "ToDo";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SelectionList.createInstance(MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //created a new userinterface object (listView) to activity_main.xml
        //created new instance (lv) of ListView
        //setting adapter for lv to display all lists with simple layout

        ListView lv = findViewById(R.id.mainListViewTaskList);

        lv.setAdapter(new ArrayAdapter<ToDoItemList>(
                this,
                android.R.layout.simple_list_item_1,
                SelectionList.getInstance().getToDoLists())
        );


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("check1", "onItemClick(" + position + ")");
                String nameOfListToOpen = SelectionList.getInstance().getToDoListByIndex(position).getListName();
                Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
                intent.putExtra("listName", nameOfListToOpen);
                startActivity(intent);
            }
        });


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // inserted quick dummy to open straight to todolist for testing.
                SelectionList.getInstance().addToDoList("testList");
                Intent nextActivity = new Intent(MainActivity.this, ToDoListActivity.class);
                nextActivity.putExtra("listName","testList" );
                startActivity(nextActivity);

           //     Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
           //             .setAction("Action", null).show();
            }
        });
    }*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/






    }

    public void addListButton (View view){
        final Context context = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add List");
        final View inflatedView = view.inflate(this, R.layout.dialog_rename_list, null);
        final EditText input = (EditText) inflatedView.findViewById(R.id.listName);
        input.setHint("List Name To Add");
        builder.setView(inflatedView);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().equals("")){
                    //implement validation error dialog

                    AlertDialog.Builder missingTitle = new AlertDialog.Builder(context);
                    missingTitle.setTitle("Cannot Create List");
                    missingTitle.setMessage("List Must have a name");
                    missingTitle.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    missingTitle.show();
                }else{
                        SelectionList.getInstance().addToDoList(input.getText().toString());
                    Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
                    intent.putExtra("listName", input.getText().toString());
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        builder.show();


    }

}
