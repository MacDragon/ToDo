package fi.metropolia.foobar.todo;

import java.io.*;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ToDoItemList {
    private ArrayList<ToDoItem> toDoList = new ArrayList<ToDoItem>();
    private String listName;
    private Context context;

    public ArrayList<ToDoItem> getToDoList() {
        return toDoList;
    }

    public void addItem(ToDoItem toDoItem){
        toDoList.add(toDoItem);
    }


    public boolean saveList(){
        // commit list to storage

        String filename = "testfile";

        Gson gson = new Gson();
        String jsonConvertedArrayList = gson.toJson(toDoList);

        Log.d(MainActivity.getTAG(), "Json data: " + jsonConvertedArrayList);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, context.MODE_PRIVATE);
            outputStream.write(jsonConvertedArrayList.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(MainActivity.getTAG(), "Exception ");
        }

        return false;

    }


    public ToDoItemList(String listName, Context context){

        this.context = context.getApplicationContext();
        //new toDoList

    }



}
