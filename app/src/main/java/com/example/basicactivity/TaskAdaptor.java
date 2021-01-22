package com.example.basicactivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

//<>list item
//Class 1.TaskAdapter. Put task_item.xml,Task.java in to recirecleview
public class TaskAdaptor extends RecyclerView.Adapter<TaskAdaptor.TaskViewHolder> {

    //Gives TaskAdapter data to use.Object variable
    private ArrayList<Task> taskList;
    //click and change the item
    private OnItemClickListener mListener;
    public int mPosition;

    public interface OnItemClickListener {
        //interface:  not object; just method
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public TaskAdaptor(ArrayList<Task> t) {
        taskList = t;
    }



    // Put task_item(cardview) and TaskViewHolder in the RecyclerView
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View e = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(e,mListener);
        return taskViewHolder;
    }
    //Put the data in TaskViewHolder into recyceview
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.checkBox.setText(task.getTaskLabel());
        holder.dueDate.setText(task.getDueDate());

    }
    //get method: How many items are in the RecyclerView
    @Override
    public int getItemCount() {
        return taskList.size();
    }









    //Class 2: TaskViewHolder
    //Identifies and sets equal to id(connective)
    //Directly commuciate with cardview
    public class TaskViewHolder extends RecyclerView.ViewHolder{


        public CheckBox checkBox;
        public TextView dueDate;

        public TaskViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            dueDate = itemView.findViewById(R.id.dueDay);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked() == true){
                        checkBox.setTextColor(Color.LTGRAY);
                        dueDate.setTextColor(Color.LTGRAY);
                        checkBox.setPaintFlags(checkBox.getPaintFlags() |Paint.STRIKE_THRU_TEXT_FLAG);
                        dueDate.setPaintFlags(dueDate.getPaintFlags() |Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                    else {
                        checkBox.setTextColor(Color.BLACK);
                        checkBox.setPaintFlags(checkBox.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        dueDate.setTextColor(Color.BLACK);
                        dueDate.setPaintFlags(dueDate.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    }


                }
            });

            //what we do yesterday, for it is able to click,
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        //which item we click
                        mPosition = position;
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);


                        }
                    }
                }
            });
        }
    }
}
