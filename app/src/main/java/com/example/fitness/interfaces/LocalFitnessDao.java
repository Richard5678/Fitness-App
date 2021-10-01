package com.example.fitness.interfaces;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitness.models.LocalFitness;

import java.util.List;

@Dao
public interface LocalFitnessDao {
    @Query("INSERT INTO LocalFitness (username, name, duration, calories, type) VALUES (:username, :name, :duration, :calories, :type)")
    void create(String username, String name, float duration, float calories, String type);

    @Query("DELETE FROM LocalFitness WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM LocalFitness WHERE username = :username")
    void deleteAll(String username);

    @Query("SELECT * FROM LocalFitness WHERE username = :username AND type = :type")
    List<LocalFitness> get(String username, String type);

    @Query("SELECT * FROM LocalFitness WHERE id = :id")
    LocalFitness getViewId(int id);

    @Query("SELECT name FROM LocalFitness WHERE username = :username")
    List<String> getNames(String username);

    @Query("SELECT * FROM LocalFitness WHERE name = :name")
    LocalFitness getSingle(String name);


    @Query("SELECT * FROM LocalFitness WHERE username = :username")
    List<LocalFitness> getAll(String username);
}
