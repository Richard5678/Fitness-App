package com.example.fitness.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitness.R;

public class SingleLocalFitness extends AppCompatActivity {
    private String name;
    private float calories;
    private float duration;
    private String type;

    private TextView fitnessName;
    private TextView fitnessCalories;
    private TextView fitnessDuration;
    private TextView fitnessType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_single_fitness);
        name = getIntent().getStringExtra("name");
        calories = getIntent().getFloatExtra("calories", 0);
        duration = getIntent().getFloatExtra("duration", 0);
        type = getIntent().getStringExtra("type");


        fitnessName = findViewById(R.id.home_single_fitness_name);
        fitnessCalories = findViewById(R.id.home_single_fitness_calories);
        fitnessDuration = findViewById(R.id.home_single_fitness_duration);
        fitnessType = findViewById(R.id.home_single_fitness_type);

        fitnessName.setText(name);
        fitnessCalories.setText(String.valueOf(calories));
        fitnessDuration.setText(String.valueOf(duration));
        fitnessType.setText(type);
    }
}
