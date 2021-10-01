package com.example.fitness.interfaces;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitness.models.LocalFood;

import java.util.List;

@Dao
public interface LocalFoodDao {
    @Query("INSERT INTO LocalFOOD (username, name, calories, protein, fat, carbs, quantity, imageURL) VALUES " +
            "(:username, :name, :calories, :protein, :fat, :carbs, :quantity, :imageURL)")
    void create(String username, String name, float calories, float protein, float fat, float carbs, float quantity, String imageURL);

    @Query("SELECT * FROM LocalFood WHERE username = :username")
    List<LocalFood> getAll(String username);

    @Query("DELETE FROM localFood WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM LocalFood WHERE username = :username")
    void deleteAll(String username);

    @Query("SELECT name FROM LocalFood WHERE username = :username")
    List<String> getAllNames(String username);

    @Query("SELECT * FROM LocalFood WHERE username = :username AND name = :name")
    LocalFood getLocalFood(String username, String name);
}
