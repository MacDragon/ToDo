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

    public ArrayList<ToDoItemList> getToDoLists(){
        //return all lists saved in the selectionList
        return selectionList;
    }

    public ToDoItemList getToDoListByIndex(int i){
        return selectionList.get(i);

    }





}
