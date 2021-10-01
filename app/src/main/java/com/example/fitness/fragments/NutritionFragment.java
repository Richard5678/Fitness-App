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

import com.example.fitness.databases.FoodDatabase;
import com.example.fitness.databases.NutritionDatabase;
import com.example.fitness.R;
import com.example.fitness.activities.MainActivity;
import com.example.fitness.adapters.NutritionAdapter;
import com.example.fitness.models.Nutrition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class NutritionFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static NutritionDatabase nutritionDatabase;
    private RecyclerView recyclerView;
    private NutritionAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String username;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private String date;
    static FoodDatabase foodDatabase;

    TextView notification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.nutrition, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.recordNutrition("nutrition", adapter);

        //get username from main activity
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");

        //get nutrition database
        nutritionDatabase = Room.databaseBuilder(getContext(), NutritionDatabase.class,
                "nutritionDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        foodDatabase = Room.databaseBuilder(getContext(), FoodDatabase.class,
                "foodDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //set up the calendar format
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

        //get current date
        date = dateFormat.format(calendar.getTime());


        notification = (TextView) view.findViewById(R.id.datePicker_notification);

        //wire up recyclerview with local property
        recyclerView = view.findViewById(R.id.nutrition_recycler_view);

        //instantiate layout manager and nutrition adapter
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new NutritionAdapter(username);

        //set up recycler view to display nutrition activities
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //add nutrition activity to the top when the plus button is clicked
        FloatingActionButton add = view.findViewById(R.id.add_NutritionActivity);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //delete the last nutrition activity when the "x" button is clicked
        FloatingActionButton delete = view.findViewById(R.id.delete_NutritionActivity);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nutritionID = nutritionDatabase.nutritionDao().getLastID();
                foodDatabase.foodDao().delete_nutritionID(nutritionID);
                nutritionDatabase.nutritionDao().deleteLast();
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
                List<Nutrition> nutritionList = nutritionDatabase.nutritionDao().getAllNutrition(username);
                Collections.reverse(nutritionList);
                int id = nutritionList.get(viewHolder.getAdapterPosition()).getId();
                nutritionDatabase.nutritionDao().delete(id);
                foodDatabase.foodDao().delete_nutritionID(id);
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




        List<String> pickedDates = nutritionDatabase.nutritionDao().getPickedDates(username);

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
            nutritionDatabase.nutritionDao().create(username, date, year, month + 1, dayOfMonth);
            Log.d("date", "year: " + year + " month: " + month + " day: " + dayOfMonth);
            adapter.reload(username);
        }


    }

    //reload nutrition activities when the nutrition fragment is resumed
    @Override
    public void onResume() {
        super.onResume();
        adapter.reload(username);
    }
}

