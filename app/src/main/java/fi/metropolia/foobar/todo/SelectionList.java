package fi.metropolia.foobar.todo;

import android.content.Context;

import java.util.ArrayList;

class SelectionList {
    private static  SelectionList ourInstance;
    private ArrayList<ToDoItemList> selectionList;
    private Context context;

    public static SelectionList getInstance() {

        return ourInstance;
    }

    private SelectionList(Context context) {
        this.context = context;
        selectionList = new ArrayList<ToDoItemList>();
        selectionList.add(new ToDoItemList("testList1", context));
    }

    public static void createInstance(Context context){
        ourInstance = new SelectionList(context);
    }

    public void addToDoList(String name){
        selectionList.add(new ToDoItemList(name, context));
    }

    public ToDoItemList getToDoList(String name){
        //for function to search through the array list to get the right list back.
        // return null if no list found
        for(ToDoItemList currentList : selectionList){
            if (currentList.getListName().equals(name)){
                return currentList;
            }
        }
        return null; //if the method has not return anything already. it will return null. 
    }





}
