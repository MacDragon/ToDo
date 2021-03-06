package fi.metropolia.foobar.todo;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;



/**
 * Singleton class to hold all the application's data in a centralized instance. It is populated once the application is started from
 * internal storage.
 */
public class SelectionList {
    private static SelectionList ourInstance;
    private String filename;
    private ArrayList<ToDoItemList> selectionList;
    private Context context;
    private ArrayList<ToDoItem> toDoList;


    public static SelectionList getInstance() {

        return ourInstance;
    }

    private SelectionList(Context context) {
        this.context = context;
        selectionList = new ArrayList<ToDoItemList>();

        populateSelectionList();

        //read all files




        /*File[] savedFiles;

        savedFiles = context.getFilesDir().listFiles();

        if (savedFiles != null)
        {
            Log.d("test2", "file read");
            for (File file : savedFiles)
            {
                // Here is each file
                String ret = "";
                Log.d("test2", "file read1");

                try {
                    InputStream inputStream = context.openFileInput(file.getName());
                    Log.d("test2", "file read2 " + file.getName());

                    if ( inputStream != null ) {
                        Log.d("test2", "file read3");
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        while ( (receiveString = bufferedReader.readLine()) != null ) {
                            stringBuilder.append(receiveString);
                        }

                        inputStream.close();
                        ret = stringBuilder.toString();
                        Log.d("test2", "file read4" + ret);
                        Gson gson = new Gson();
                        Log.d("test2", "file read5");
                        Type listType;
                        listType = new TypeToken<ArrayList<ToDoItem>>() { }.getType();

                        toDoList = gson.fromJson(ret, listType);
                        Log.d("test2", "file read6");

                        selectionList.add(new ToDoItemList(file.getName(), toDoList));







                    }
                }
                catch (Exception e) {
                    Log.e("test2", "File not found: " + e.toString());
                    selectionList.add(new ToDoItemList("testList1", context));
                    selectionList.add(new ToDoItemList("toDoList0", context));
                    // for testing purposes of the main activity. creating multiple separate task lists
                    //read fileList directly and populate array from their  Hint: to call fileList() method from Context class
                    selectionList.add(new ToDoItemList("testList2", context));
                    selectionList.add(new ToDoItemList("testList3", context));
                    selectionList.add(new ToDoItemList("testList4", context));
                    selectionList.add(new ToDoItemList("testList5", context));
                    selectionList.add(new ToDoItemList("testList6", context));
                    selectionList.add(new ToDoItemList("testList7", context));
                    selectionList.add(new ToDoItemList("testList8", context));
                    selectionList.add(new ToDoItemList("testList9", context));
                    selectionList.add(new ToDoItemList("testList10", context));
                }


            }






            }else{
            Log.d("test2", "no file read");
        }*/


    }

    public static void createInstance(Context context) {
        ourInstance = new SelectionList(context);
    }

    /**
     * method to add new ToDoList
     * @param name
     */
    public void addToDoList(String name) {
        selectionList.add(new ToDoItemList(name, context));
    }
    /*
    method to search for specific list by its name
     */
    public ToDoItemList getToDoList(String name) {
        //for function to search through the array list to get the right list back.
        // return null if no list found
        for (ToDoItemList currentList : selectionList) {
            if (currentList.getListName().equals(name)) {
                return currentList;
            }
        }
        return null; //if the method has not return anything already. it will return null.
    }
    /**
    method to return selectionList.
     */
    public ArrayList<ToDoItemList> getToDoLists() {
        //return all lists saved in the selectionList
        return selectionList;
    }
    /**
     * get specific list by its index from selectionList
     */
    public ToDoItemList getToDoListByIndex(int i) {
        return selectionList.get(i);

    }

    /**
     * method to check particular ToDoList exist or not.
     * @param listName
     * @return
     */

    public boolean listExists(String listName) {
        for (ToDoItemList currentList : selectionList) {
            if (currentList.getListName().equals(listName)) {
                return true;
            }

        }
        return false;
    }


    /**
     * method to intialize selectionList by reading file names from the internal storage.
     */
    public void populateSelectionList() {

        String[] listNames = context.fileList();
        Arrays.sort(listNames);

        for (String listName : listNames) {
            selectionList.add(new ToDoItemList(listName, context));
        }
    }


    /**
     * method to check if the selectionList is empty or not.
     * @return
     */
    public boolean isEmpty() {
        if (this.selectionList.isEmpty()) {
            return true;

        } else {
            return false;
        }
    }


    /**
     * method to delete particular list form SelectionList.
     * @param listName
     */

    public void deleteList(String listName) {
        this.selectionList.remove(getToDoList(listName));
        File[] savedFiles;

        savedFiles = context.getFilesDir().listFiles();

        if (savedFiles != null) {
            Log.d("test2", "file read");
            for (File file : savedFiles) {
                // Here is each file

                if (file.getName().equals(listName)) {
                    file.delete();

                }


            }


        }
    }
}
