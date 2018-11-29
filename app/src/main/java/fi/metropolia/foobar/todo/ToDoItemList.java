package fi.metropolia.foobar.todo;

import java.io.*;
import java.lang.reflect.Type;
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
    private ArrayList<ToDoItem> toDoList;
    // name of list, which will be same as filename to store list.
    private String listName;
    // context needed to be able to get file handle for saving, this is passed on creation.
    private Context context;

    /**
     * Return this class instances list of todo items
     *
     * @return
     */

    public ArrayList<ToDoItem> getToDoListArray() {
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
    public boolean saveList(){ // not yet being called, initial implementation
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
            Log.d(MainActivity.getTAG(), "Exception in file saving");
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
        this.listName = listName;

        FileInputStream inputStream;
        String fileData = "";

        try {
            inputStream = context.openFileInput(listName);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                fileData = bufferedReader.readLine();
                inputStream.close();

                Type listType = new TypeToken<ArrayList<ToDoItem>>() { }.getType();
                Gson gson = new Gson();
                toDoList = gson.fromJson(fileData, listType);

                Log.d(MainActivity.getTAG(), "Json data: " + fileData);
            }

        } catch (Exception e) {
            // file read error, create new emptylist, populate with dummy values for now.
            toDoList = new ArrayList<ToDoItem>();

            toDoList.add(new ToDoItem("Test", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test2", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test3", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test4", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test5", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test6", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test7", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test8", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test9", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test10", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test11", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test12", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test13", "Nothing", false, false));
            toDoList.add(new ToDoItem("Test14", "Nothing", false, false));

        }

        //new toDoList

    }



}
