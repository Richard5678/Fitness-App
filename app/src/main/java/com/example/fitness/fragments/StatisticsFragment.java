package com.example.fitness.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.fitness.databases.ExerciseDatabase;
import com.example.fitness.databases.FitnessDatabase;
import com.example.fitness.databases.FoodDatabase;
import com.example.fitness.databases.LocalFoodDatabase;
import com.example.fitness.R;
import com.example.fitness.databases.NutritionDatabase;
import com.example.fitness.models.Fitness;
import com.example.fitness.models.Nutrition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.widget.Toast;

public class StatisticsFragment extends Fragment {
    String username;
    LineChart chartCalorieIntakeWeek;
    LineChart chartCalorieOutputWeek;

    LineChart chartProteinWeek;
    LineChart chartFatWeek;
    LineChart chartSugarWeek;

    int year;
    int month;
    int day;
    FitnessDatabase fitnessDatabase;
    ExerciseDatabase exerciseDatabase;

    NutritionDatabase nutritionDatabase;
    FoodDatabase foodDatabase;

    List<Entry> entries;
    List<Fitness> fitnessList = new ArrayList<>();
    List<Nutrition> nutritionList = new ArrayList<>();
    List<Float> caloriesIntake = new ArrayList<>();
    List<Float> caloriesOutput = new ArrayList<>();

    int maxX;
    int minX;

    private LocalFoodDatabase localFoodDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics, container, false);

        //get the username from main activity
        Bundle bundle = this.getArguments();
        username = bundle.getString("username");


        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        chartCalorieIntakeWeek = (LineChart) view.findViewById(R.id.week_caloriesIntake);
        chartCalorieOutputWeek = (LineChart) view.findViewById(R.id.week_caloriesOutput);

        entries = new ArrayList<Entry>();

        //get fitness database
        fitnessDatabase = Room.databaseBuilder(getContext(), FitnessDatabase.class,
                "fitnessDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //get nutrition database
        nutritionDatabase = Room.databaseBuilder(getContext(), NutritionDatabase.class,
                "nutritionDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        //get food database
        foodDatabase = Room.databaseBuilder(getContext(), FoodDatabase.class,
                "foodDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        exerciseDatabase = Room.databaseBuilder(getContext(), ExerciseDatabase.class,
                "exerciseDatabase").fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        fitnessList = fitnessDatabase.fitnessDao().getIDsWeek(username);

        for (int i = fitnessList.size() - 1; i >= 0; i--) {
            caloriesOutput = exerciseDatabase.exerciseDao().getCalories(fitnessList.get(i).getId());
            float sumCalories = 0;
            for (int m = 0; m < caloriesIntake.size(); m++) {
                sumCalories += caloriesIntake.get(m);
            }
            if (i == fitnessList.size() - 1) {
                maxX = fitnessList.get(i).getDay();
                minX = fitnessList.get(i).getDay();
            }

            if (fitnessList.get(i).getDay() > maxX) {
                maxX = fitnessList.get(i).getDay();
            }
            if (fitnessList.get(i).getDay() < minX) {
                minX = fitnessList.get(i).getDay();
            }

            entries.add(new Entry((float)fitnessList.get(i).getDay(), sumCalories));
        }

        LineDataSet dataSet = new LineDataSet(entries, "caloriesOutput");
        dataSet.setColor(0xFF0265fa);
        dataSet.setValueTextColor(0xFF0f0f0f);
        dataSet.setValueTextSize(10);

        YAxis yAxis = chartCalorieOutputWeek.getAxisLeft();
        yAxis.setAxisMinimum(0);
        yAxis.setDrawGridLines(false);
        chartCalorieOutputWeek.getAxisRight().setEnabled(false);

        XAxis xAxis = chartCalorieOutputWeek.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(maxX - minX);

        Description description = chartCalorieOutputWeek.getDescription();
        description.setText("date");
        description.setTextSize(30);

        LineData data = new LineData(dataSet);
        chartCalorieOutputWeek.setData(data);
        chartCalorieOutputWeek.invalidate();


        nutritionList = nutritionDatabase.nutritionDao().getIDsWeek(username);

        for (int i = nutritionList.size() - 1; i >= 0; i--) {
            caloriesIntake = foodDatabase.foodDao().getCalories(nutritionList.get(i).getId());
            float sumCalories = 0;
            for (int m = 0; m < caloriesIntake.size(); m++) {
                sumCalories += caloriesIntake.get(m);
            }
            if (i == nutritionList.size() - 1) {
                maxX = nutritionList.get(i).getDay();
                minX = nutritionList.get(i).getDay();
            }

            if (nutritionList.get(i).getDay() > maxX) {
                maxX = nutritionList.get(i).getDay();
            }
            if (nutritionList.get(i).getDay() < minX) {
                minX = nutritionList.get(i).getDay();
            }

            entries.add(new Entry((float)nutritionList.get(i).getDay(), sumCalories));
        }

        LineDataSet data1 = new LineDataSet(entries, "caloriesIntake");
        dataSet.setColor(0xFF0265fa);
        dataSet.setValueTextColor(0xFF0f0f0f);
        dataSet.setValueTextSize(10);

        YAxis y = chartCalorieIntakeWeek.getAxisLeft();
        y.setAxisMinimum(0);
        y.setDrawGridLines(false);
        chartCalorieIntakeWeek.getAxisRight().setEnabled(false);

        XAxis x = chartCalorieIntakeWeek.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setLabelCount(maxX - minX);

        Description descri = chartCalorieIntakeWeek.getDescription();
        descri.setText("date");
        descri.setTextSize(30);

        LineData data2 = new LineData(data1);
        chartCalorieIntakeWeek.setData(data2);
        chartCalorieIntakeWeek.invalidate();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}