package com.example.fitness.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import java.util.List;

@Entity(tableName = "LocalFitness")
public class LocalFitness {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "duration")
    private float duration;

    @ColumnInfo(name = "calories")
    private float calories;

    @ColumnInfo(name = "type")
    private String type;

    public LocalFitness(String username, String name, float duration, float calories, String type) {
        this.username = username;
        this.name = name;
        this.duration = duration;
        this.calories = calories;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }

    public String getName() {
        return name;
    }

    public float getCalories() {
        return calories;
    }

    public float getDuration() {
        return duration;
    }

    public String getType() { return type; }

}
