package fi.metropolia.foobar.todo;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Class to store each individual ToDoList by the global singleton, and used by each activity.
 * https://github.com/google/gson/blob/master/UserGuide.md used to aid easily saving and repopulating data from files.
 */

public class ToDoItemList {
    // ArrayList of ToDoitems, initialise to blank array at instancing
    private ArrayList<ToDoItem> toDoList;
    // name of list, which will be same as filename to store list.
    private String listName;
    // context needed to be able to get file handle for saving, this is passed on creation.
    private Context context;

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

    /**
     * returns number of items in list.
     * @return
     */

    public int size(){
      return toDoList.size();
    }

    /**
     * add a new item object into the list
     * @param item
     * @return
     */
    public boolean add(ToDoItem item){

        return toDoList.add(item);
    }

    /**
     * add new item object into the list at specific location
     * @param index
     * @param item
     */

    public void add(int index, ToDoItem item){
        toDoList.add(index, item);
    }

    /**
     * remove an item from specified location.
     * @param index
     * @return
     */

    public ToDoItem remove(int index){
        return toDoList.remove(index);
    }

    /**
     * remove a specified object from the list.
     * @param item
     * @return
     */

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


    /**
     * Check if the list file for this instance exists, so that we can d
     * @return
     */

    public boolean listFileExists(){
        File listFile =  context.getFileStreamPath(this.listName);
        File newListFile = new File(listFile.getParent(), listName);
        if (newListFile.exists()){
            return true;
        }
        return false;

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

        // only save the list if the file actually exists
        // if it doesn't, the list has been deleted and no action should be taken.
        if ( listFileExists() ) {

            // string to hold JSON data.

            Log.d(MainActivity.getTAG(), "saveList: " + listName);

            // convert the list data to a Json string using google Gson object
            String jsonConvertedArrayList = new Gson().toJson(toDoList);

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
                Log.d(MainActivity.getTAG(), "Exception in file saving" + e.toString());
                return false;
            }

        }
        return false; // file did not save
    }

    /**
     * provide the lists title as default toString method.
     * @return returns the lists name.
     */
    @Override
    public String toString() {
        return listName;
    }

/*
    public void addToDoItem(ToDoItem toDoItem){
        toDoList.add(toDoItem);
        saveList();
    }


    public void removeToDoItem(int index){
        toDoList.remove(index);
    }
*/
    /**
     * Load ( or create if it doesn't already exist ) a list file.
     * ListName must not be blank.
     *
     * @param listName name for list to load or create, must not be empty.
     * @param context pass context into the object in order to allow file access.
     */


    public ToDoItemList(String listName, Context context){
        // store the applications context for to use for file access on saving
        this.context = context.getApplicationContext();
        this.listName = listName;

        // object to define file
        FileInputStream inputStream;

        if( listFileExists() ) {
            // the file already exists, lets load it.
            try { // attempt to open and read the file.
                inputStream = context.openFileInput(listName);

                if (inputStream != null) { // if we managed to open file try to read it
                    // create input stream reader to convert byte data from file to char data for string
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    // create bufferedreader to read the actual data as it has a method to read a whole line at once.
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    // read line of data from file
                    // as we are only saving a single string to file there will only be one line
                    // in a more complex application where the user may have loaded the file from external source
                    // the file the whole file would have to be parsed through
                    String fileData = bufferedReader.readLine();
                    // close the file now that reading has been done.
                    inputStream.close();
                    // TypeToken creates a representation of the ArrayList of ToDoItem objects
                    Type listType = new TypeToken<ArrayList<ToDoItem>>() { }.getType();
                    // use a Gson object to create the ArrayList from the input string using generated Type definition.
                    toDoList = new Gson().fromJson(fileData, listType);

                }

            } catch (Exception e) {
                Log.e(MainActivity.getTAG(), "Exception reading file: " + e.toString());
                // file read error, file doesn't exist. Create new emptylist
                // populate with dummy values for quick testing purposes.

            }

        } else {
            // file doesn't exist, lets create it and initialise a new blank ToDoList

            toDoList = new ArrayList<ToDoItem>();
            FileOutputStream outputStream;
            // attempt to create empty file for list
            try {
                // attempt to open and close file for writing to create it.
                outputStream = context.openFileOutput(listName, context.MODE_PRIVATE);;
                outputStream.close();
            } catch (Exception e) {
                // log error on creating file
                Log.d(MainActivity.getTAG(), "Exception in file saving" + e.toString());
            }


        }

    }





}
