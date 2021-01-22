package com.example.basicactivity;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    //object variables
    private RecyclerView recyclerView;
    private TaskAdaptor taskAdaptor;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Task> taskList;



    //
    private Task deletedMovie = null;
    //left
    private ArrayList<Task> archiveMovies = new ArrayList<>();
    private String calendar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //2 method red
        //taskList = something
        createTaskList();

        //recyclerView = something
        createRecyclerView();


        //main for pop_up_window
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.pop_up_window_change, null);
                final EditText taskLabel = (EditText) mView.findViewById(R.id.taskLabel);
                //final EditText dueDate = (EditText) mView.findViewById(R.id.dueDate);//
                mBuilder.setView(mView);
                //copy from outside
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                ImageView mSubmit = mView.findViewById(R.id.submit_you);

               // Button mDueDate = (Button) mView.findViewById(R.id.dueDate);

                ImageView mCalendar =(ImageView) mView.findViewById(R.id.calendar);

                mCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogFragment datePicker = new DatePickerFragment();
                        datePicker.show(getSupportFragmentManager(),"date picker");

                    }
                });


//                    mDueDate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        DialogFragment datePicker = new DatePickerFragment();
//                        datePicker.show(getSupportFragmentManager(),"date picker");
//                    }
//                });



                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!taskLabel.getText().toString().isEmpty() && calendar != null
                        ) {
                            //insert new item
                            insertTask(new Task(taskLabel.getText().toString(), calendar, false));
                            Toast.makeText(MainActivity.this, "Added",Toast.LENGTH_SHORT).show();
                            //when finish, then close
                            dialog.cancel();

                        }else{
                            Toast.makeText(MainActivity.this, "Missing Input" +
                                    "", Toast.LENGTH_SHORT).show();
                        }

                    }
                });





            }
        });

    }




    //up down, right left
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |ItemTouchHelper.DOWN |ItemTouchHelper. START | ItemTouchHelper.END,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

           int fromPositionn = viewHolder.getAdapterPosition();
           int toPosition = target.getAdapterPosition();

            Collections.swap(taskList, fromPositionn, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPositionn, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            //Just focus on this
            switch (direction) {
                //if this is true
                case ItemTouchHelper.LEFT:
                    //Do this
                    deletedMovie = taskList.get(position);
                    deleteTask(position);
                    Snackbar.make(recyclerView, deletedMovie.getTaskLabel(), Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    taskList.add(position, deletedMovie);
                                    taskAdaptor.notifyItemInserted(position);

                                }
                            }).show();


                    break;

                case ItemTouchHelper.RIGHT:
                    final Task movieName = taskList.get(position);
                    archiveMovies.add(movieName);
                    deleteTask(position);
                    Snackbar.make(recyclerView, movieName.getTaskLabel()+",Archived", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    archiveMovies.remove(archiveMovies.lastIndexOf(movieName));
                                    taskList.add(position, movieName);
                                    taskAdaptor.notifyItemInserted(position);



                                }
                            }).show();


                    break;
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(MainActivity.this,c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent))
                    .addSwipeRightActionIcon(R.drawable.ic_archive_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    //
    //insertTask(Task t) t = String taskLable, String dueDate, boolean isComplete
    public void insertTask(Task t){
        taskList.add(t);
        taskAdaptor.notifyItemInserted(taskList.size()-1);
        //0,1,2,3
        //size 4
        //size - 1 = 3

    }
    //deleteTask(int=number position=1,2,3,4,)
    public void deleteTask(int position){
        taskList.remove(position);
        taskAdaptor.notifyItemRemoved(position);
    }
    //changeTask(Task t, int position)
    public void changeTask(String taskName, String dueDate, int position) {
        taskList.get(position).setTaskLabel(taskName);
        taskList.get(position).setDueDate(dueDate);
        taskAdaptor.notifyItemChanged(position);
    }



    //createTaskList()
    public void createTaskList() {
        taskList = new ArrayList<>();

//        for(int i = 0; i <20; i++) {
        //taskList.add(new Task("Task "+i, "Jun", false));
//    }

    }

    //createRecyclerView()
    public void createRecyclerView(){
        //Identify and set the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        //Identify and set the TaskAdapter and RecyeclerView.LayoutManager(context main activity)
        layoutManager = new LinearLayoutManager(this);
        taskAdaptor = new TaskAdaptor(taskList);

        //Associate,connect both to ReceylcerView
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(taskAdaptor);



        // for swip down up right left
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);




        taskAdaptor.setOnItemClickListener(new TaskAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.pop_up_window_add, null);
                final EditText taskLabel = (EditText) mView.findViewById(R.id.taskLabel);
                //final EditText dueDate = (EditText) mView.findViewById(R.id.dueDate);//
                mBuilder.setView(mView);
                //copy from outside
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                ImageView mSubmit = mView.findViewById(R.id.submit_you);
                ImageView mCalendar =(ImageView) mView.findViewById(R.id.calendar);

                mCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogFragment datePicker = new DatePickerFragment();
                        datePicker.show(getSupportFragmentManager(),"date picker");

                    }
                }); 
               // Button mDueDate = (Button) mView.findViewById(R.id.dueDate);


//               // mDueDate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        DialogFragment datePicker = new DatePickerFragment();
//                        datePicker.show(getSupportFragmentManager(),"date picker");
//
//                    }
//                });


                mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!taskLabel.getText().toString().isEmpty() && calendar != null){
                            //insert new item
                            changeTask(taskLabel.getText().toString(), calendar, taskAdaptor.mPosition);
                            Toast.makeText(MainActivity.this, "Added",Toast.LENGTH_SHORT).show();
                            //when finish, then clos
                            dialog.cancel();

                        }else{
                            Toast.makeText(MainActivity.this, "Missing Input", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());



    }
}



