package fi.metropolia.foobar.todo;

import java.io.*;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Class to store each ToDo List
 */
public class ToDoItemList {
    // ArrayList of ToDoitems, initialise to blank array at instancing
    private ArrayList<ToDoItem> toDoList = new ArrayList<ToDoItem>();
    // name of list, which will be same as filename to store list.
    private String listName;
    // context needed to be able to get file handle for saving, this is passed on creation.
    private Context context;

    /**
     * Return this class instances list of todo items
     *
     * @return
     */

    public ArrayList<ToDoItem> getToDoList() {
        return toDoList;
    }

    /**
     * Adds a new item to stored list.
     * @param toDoItem
     */

    public void addItem(ToDoItem toDoItem){
        toDoList.add(toDoItem);
    }


    /**
     * Returns a ToDo Lists name.
     * @return
     */

    public String getListName() {
        return listName;
    }

    /**
     * Returns an individual item at specified index from ToDo List
     * @param index
     * @return
     */

    public ToDoItem getToDoItem(int index){
        return toDoList.get(index);
    }


    /**
     * method to save list to storage
     * @return
     */
    public boolean saveList(){
        // create google gson object to convert array list into a JSON string easily.

        Gson gson = new Gson();

        // string to hold JSON data.

        String jsonConvertedArrayList = gson.toJson(toDoList);

        Log.d(MainActivity.getTAG(), "Json data: " + jsonConvertedArrayList);


        // create file object

        FileOutputStream outputStream;

        // attempt to write the file to

        try {
            // attempt to open file for writing.
            outputStream = context.openFileOutput(listName, context.MODE_PRIVATE);
            // write out the whole string to file, as we have not seeked to end of file this will replace existing contents.
            outputStream.write(jsonConvertedArrayList.getBytes());
            outputStream.close();
            return true; // file saved successfully
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(MainActivity.getTAG(), "Exception ");
        }

        return false; // file did not save
    }


    /**
     * Load ( or create if it doesn't already exist ) a new list.
     * ListName must not be blank.
     *
     * @param listName name for list to load or create, must not be empty.
     * @param context
     */

    public ToDoItemList(String listName, Context context){

        this.context = context.getApplicationContext();
        //new toDoList

    }



}
