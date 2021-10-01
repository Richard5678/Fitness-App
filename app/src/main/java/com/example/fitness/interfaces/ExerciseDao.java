package com.example.fitness.interfaces;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitness.models.Exercise;
import com.example.fitness.models.LocalFitness;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("INSERT INTO Exercises (fitnessId, name, calories, duration, type) VALUES " +
            "(:fitnessId, :name, :calories, :duration, :type)")
    void create(int fitnessId, String name, float calories, float duration, String type);

    @Query("SELECT * FROM Exercises WHERE fitnessId = :fitnessID")
    List<Exercise> getAll(int fitnessID);

    @Query("DELETE FROM Exercises WHERE id = (SELECT MAX(id) FROM Exercises WHERE fitnessId = :fitnessId)")
    void deleteLast(int fitnessId);

    @Query("SELECT calories FROM Exercises WHERE fitnessId = :id")
    List<Float> getCalories(int id);
}
