package com.example.fitness.fragments;

import android.app.DatePickerDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.fitness.databases.FitnessDatabase;
import com.example.fitness.R;
import com.example.fitness.activities.SingleFitness;
import com.example.fitness.activities.MainActivity;
import com.example.fitness.adapters.FitnessAdapter;
import com.example.fitness.models.Fitness;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FitnessFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static FitnessDatabase fitnessDatabase;
    private RecyclerView recyclerView;
    private FitnessAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String username;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private String date;
    private int year;
    private int month;
    private int day;

    TextView notification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fitness, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.recordFitness("fitness", adapter);

        //get username from main activity
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");


        //get fitness database
        fitnessDatabase = Room.databaseBuilder(getContext(), FitnessDatabase.class,
                "fitnessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //set up the calendar format
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

        //get current date
        date = dateFormat.format(calendar.getTime());


        notification = (TextView) view.findViewById(R.id.datePicker_notification);

        //wire up recyclerview with local property
        recyclerView = view.findViewById(R.id.fitness_recycler_view);

        //instantiate layout manager and fitness adapter
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new FitnessAdapter(username);

        //set up recycler view to display fitness activities
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //add fitness activity to the top when the plus button is clicked
        FloatingActionButton add = view.findViewById(R.id.add_fitnessActivity);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //delete the last fitness activity when the "x" button is clicked
        FloatingActionButton delete = view.findViewById(R.id.delete_fitnessActivity);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fitnessID = fitnessDatabase.fitnessDao().getLastID();
                SingleFitness.foodDatabase.foodDao().delete_nutritionID(fitnessID);
                fitnessDatabase.fitnessDao().deleteLast();
                adapter.reload(username);
            }
        });

        //allow user to swipe to delete
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<Fitness> fitnessList = fitnessDatabase.fitnessDao().getAllFitness(username);
                int id = fitnessList.get(viewHolder.getAdapterPosition()).getId();
                fitnessDatabase.fitnessDao().delete(id);
                adapter.reload(username);
            }
        });
        helper.attachToRecyclerView(recyclerView);

        return view;
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth, this,
                Calendar.getInstance().get(Calendar.YEAR) ,
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    //set and display date on the top of the screen
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = dateFormat.format(c.getTime());




        List<String> pickedDates = fitnessDatabase.fitnessDao().getPickedDates(username);

        Boolean datePicked = false;
        for (String d : pickedDates) {
            if (d.equals(date)) {
                datePicked = true;
                notification.setVisibility(View.VISIBLE);
                notification.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notification.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
                break;
            }
        }

        if (!datePicked) {
            fitnessDatabase.fitnessDao().create(username, date, year, month + 1, dayOfMonth);
            Log.d("date", "year: " + year + " month: " + month + " day: " + dayOfMonth);
            adapter.reload(username);
        }


    }

    //reload fitness activities when the fitness fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        adapter.reload(username);
    }
}

