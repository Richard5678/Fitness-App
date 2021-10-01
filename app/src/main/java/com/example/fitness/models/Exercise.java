package com.example.fitness.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Exercises")
public class Exercise {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "FitnessId")
    private int fitnessId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "calories")
    private float calories;

    @ColumnInfo(name = "duration")
    private float duration;

    @ColumnInfo(name = "type")
    private String type;

    public Exercise(int fitnessId, String name, float calories, float duration, String type) {
        this.fitnessId = fitnessId;
        this.name = name;
        this.calories = calories;
        this.duration = duration;
        this.type = type;
    }

    public int getFitnessId() {
        return fitnessId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getCalories() {
        return calories;
    }

    public float getDuration() {
        return duration;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}