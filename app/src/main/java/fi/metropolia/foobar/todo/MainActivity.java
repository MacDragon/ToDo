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

public class MainActivity extends AppCompatActivity{
    private Context context;
    private ArrayAdapter<ToDoItemList> arrayAdapter;

    public static String getTAG() {
        return "ToDo";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SelectionList.createInstance(MainActivity.this);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //created a new userinterface object (listView) to activity_main.xml
        //created new instance (lv) of ListView
        //setting adapter for lv to display all lists with simple layout
        arrayAdapter = new ArrayAdapter<ToDoItemList>(
                this,
                android.R.layout.simple_list_item_1,
                SelectionList.getInstance().getToDoLists()
        );

        ListView lv = findViewById(R.id.mainListViewTaskList);

        lv.setAdapter(arrayAdapter);


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


        if(SelectionList.getInstance().isEmpty()){
            //dialog box similar to one
            addListButton(lv);

        }

    }

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

        @Override

        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

       public void preferenceClick(MenuItem item){
           Intent nextActivity = new Intent(MainActivity.this, SettingsActivity.class);
           startActivity(nextActivity);

       }









    @Override
    protected void onResume(){
        super.onResume();
        arrayAdapter.notifyDataSetChanged();

    }

    @Override
    protected  void onPause(){
        super.onPause();
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
                    displayErrorDialog("Cannot Create List", "List Must have a name");

                }else{
                    if(SelectionList.getInstance().listExists(input.getText().toString())){
                        displayErrorDialog("Cannot Create List", "List already exists");
                    }else{
                        SelectionList.getInstance().addToDoList(input.getText().toString());
                        Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
                        intent.putExtra("listName", input.getText().toString());
                        startActivity(intent);
                    }

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

    private void displayErrorDialog(String title, String message){
        AlertDialog.Builder missingTitle = new AlertDialog.Builder(context);
        missingTitle.setTitle(title);
        missingTitle.setMessage(message);
        missingTitle.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        missingTitle.show();
    }

}
