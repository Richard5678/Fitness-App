package com.example.fitness.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitness.interfaces.LocalFoodDao;
import com.example.fitness.models.LocalFood;

@Database(entities = {LocalFood.class}, version = 2)
public abstract class LocalFoodDatabase extends RoomDatabase {
    public abstract LocalFoodDao localFoodDao();
}
