package com.example.fitness.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitness.interfaces.ExerciseDao;
import com.example.fitness.models.Exercise;

@Database(entities = {Exercise.class}, version = 1)
public abstract class ExerciseDatabase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();
}
