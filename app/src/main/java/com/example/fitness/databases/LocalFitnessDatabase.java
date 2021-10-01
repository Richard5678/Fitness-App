package com.example.fitness.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitness.interfaces.LocalFitnessDao;
import com.example.fitness.models.LocalFitness;

@Database(entities = {LocalFitness.class}, version = 3)
public abstract class LocalFitnessDatabase extends RoomDatabase {
    public abstract LocalFitnessDao localFitnessDao();
}
