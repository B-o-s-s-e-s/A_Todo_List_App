package com.example.basicactivity;

import java.util.ArrayList;

public class TaskSection {

    //Object
    private String title;
    private ArrayList<Task> babyList;

    //Constructor
    public TaskSection (String t, ArrayList<Task> b){
        title = t;
        babyList = b;
    }
    //Get

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Task> getBabyList() {
        return babyList;
    }

    public void setBabyList(ArrayList<Task> babyList) {
        this.babyList = babyList;
    }

    //Set
}
