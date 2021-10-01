package com.example.fitness.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitness.interfaces.NutritionDao;
import com.example.fitness.models.Nutrition;

@Database(entities = {Nutrition.class}, version = 1)
public abstract class NutritionDatabase extends RoomDatabase {
    public abstract NutritionDao nutritionDao();
}