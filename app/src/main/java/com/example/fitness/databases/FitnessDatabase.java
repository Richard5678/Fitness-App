package com.example.fitness.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitness.interfaces.FitnessDao;
import com.example.fitness.models.Fitness;

@Database(entities = {Fitness.class}, version = 5)
public abstract class FitnessDatabase extends RoomDatabase {
    public abstract FitnessDao fitnessDao();
}
