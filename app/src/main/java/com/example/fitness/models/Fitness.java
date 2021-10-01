package com.example.fitness.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Fitness")
public class Fitness {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "day")
    private int day;

    @ColumnInfo(name = "contents")
    private String contents;

    public Fitness(String username, String date, int year, int month, int day) {
        this.username = username;
        this.date = date;
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }

    public int getYear() { return year; }

    public int getMonth() { return month; }

    public int getDay() { return day; }

    public String getDate() {
        return date;
    }

    public String getContents() {
        return contents;
    }
    public void setContents(String contents) { this.contents = contents; }
}
