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
    private boolean deleted;

    /**
     * Return this class instances list of todo items, primarily to use for array adapter.
     *
     * @return
     */

    public ArrayList<ToDoItem> getToDoListArray() {
        return toDoList;
    }

    public boolean itemExists(String item){
        for (ToDoItem i:toDoList) {
            if(i.getTitle().equals(item)){
                return true;
            }
        }
        return false;

    }

    /**
     * Adds a new item to stored list.
     * @param toDoItem
     */

    public void addItem(ToDoItem toDoItem){
        toDoList.add(toDoItem);
    }

    public int size(){
      return toDoList.size();
    }

    public boolean add(ToDoItem item){

        return toDoList.add(item);
    }

    public void delete(){
        deleted = true;
    }

    public void add(int index, ToDoItem item){
        toDoList.add(index, item);
    }

    public ToDoItem remove(int index){
        return toDoList.remove(index);
    }

    public boolean remove(ToDoItem item){
        return toDoList.remove(item);
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

    /** save list with specific name, which can rename file if new name.
     *
     * @param listName
     * @return
     */

    public boolean saveList(String listName){
        if ( !this.listName.equals(listName)){
            // rename file because listName has changed. If name same as before, just call regular savelist
            // if new name exists return false and do nothing.
            Log.d(MainActivity.getTAG(), "saveList: with listname");
            File listFile =  context.getFileStreamPath(this.listName);
            File newListFile = new File(listFile.getParent(), listName);
            if (newListFile.exists()){
                // trying to rename list to something that already exists, abandon.
                return false;
            }
            if (listFile.renameTo(newListFile) ) {
                // rename succeeded, so lets change title.
                this.listName = listName;
                saveList();
                return true;
            }

            // attempt to rename the file
        } else {
            Log.d(MainActivity.getTAG(), "saveList: else");
            saveList(); // nothing changed in name, proceed as usual.
        }
        return true;
    }

    /**
     * method to save list to storage
     * @return
     */
    public boolean saveList(){ // not yet being called, initial implementation
        // create google gson object to convert array list into a JSON string easily.
        if ( !deleted ) {

        Gson gson = new Gson();

        // string to hold JSON data.

        Log.d(MainActivity.getTAG(), "saveList: "+listName);

        String jsonConvertedArrayList = gson.toJson(toDoList);

        Log.d(MainActivity.getTAG(), "Json data: " + jsonConvertedArrayList);

        // create file streaming object

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
        }

        return false; // file did not save
    }


    @Override
    public String toString() {
        return listName;
    }


    public void addToDoItem(ToDoItem toDoItem){
        toDoList.add(toDoItem);
        saveList();
    }


    public void removeToDoItem(int index){
        toDoList.remove(index);
    }

    /**
     * Load ( or create if it doesn't already exist ) a new list.
     * ListName must not be blank.
     *
     * @param listName name for list to load or create, must not be empty.
     * @param context
     */


    public ToDoItemList(String listName, Context context){
        deleted = false;

        this.context = context.getApplicationContext();
        this.listName = listName;

        // object to define file
        FileInputStream inputStream;

        try { // attempt to open file, fall back to catch section if there is a read error ( file doesn't exist )
            inputStream = context.openFileInput(listName);

            if ( inputStream != null ) { // if we managed to open file try to read it
                // create input stream reader to convert byte data from file to char data for string
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                // create bufferedreader to read the actual data
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                // read line of data from file, as we are only saving a single string to file, there will only be one line
                // in a more complex application the whole file would have to be parsed through
                String fileData = bufferedReader.readLine();
                inputStream.close();

                // TypeToken creates a representation of the arraylist of ToDoItem objects
                // that Gson needs to be able to create the arraylist from the input string.
                Type listType;
                listType = new TypeToken<ArrayList<ToDoItem>>() { }.getType();
                Gson gson = new Gson();
                toDoList = gson.fromJson(fileData, listType);


      //          Log.d(MainActivity.getTAG(), "Json data: " + fileData);
            }

        } catch (Exception e) {
            Log.e("test3", "File not found: " + e.toString());
            // file read error, file doesn't exist. Create new emptylist
            // populate with dummy values for quick testing purposes.

            toDoList = new ArrayList<ToDoItem>();
// ask eemeli to update todoeditor to use addnewtodoitem rather than directly calling add method.
     /*       addToDoItem(new ToDoItem("Test", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test2", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test3", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test4", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test5", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test6", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test7", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test8", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test9", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test10", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test11", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test12", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test13", "Nothing", false, false));
            addToDoItem(new ToDoItem("Test14", "Nothing", false, false));*/

        }

    }





}
