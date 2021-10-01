package com.example.fitness.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitness.interfaces.FoodDao;
import com.example.fitness.models.Food;

@Database(entities = {Food.class}, version = 6)
public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();
}
