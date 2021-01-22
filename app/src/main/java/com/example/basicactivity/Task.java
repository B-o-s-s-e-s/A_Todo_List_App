package com.example.basicactivity;
//store data: // 4 part(Object type, constructor, get methods, ste methods)
//Question: where do we use these data? onBindViewHolder?
public class Task {

    //object Variables
    private String taskLabel;
    private String dueDate;
    private boolean isComplete;

    //Constructor: =something
    public Task(String t, String d, boolean i)
    {
        taskLabel = t;
        dueDate = d;
        isComplete = i;
    }

    //Get Methods: give variable
    public String getTaskLabel()
    {
        return taskLabel;
    }

    public String getDueDate()
    {
        return dueDate;
    }

    public boolean getIsComplete()
    {
        return isComplete;
    }

    //Set Methods:change,void
    public void setTaskLabel(String str) {
        taskLabel = str;
    }
    public void setDueDate(String str) {
        dueDate = str;
    }
    public void setIsComplete(boolean bool){
        isComplete = bool;
    }
}
