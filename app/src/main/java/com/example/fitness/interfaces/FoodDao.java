package com.example.fitness.interfaces;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitness.models.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("INSERT INTO Food (nutritionID, meal, name, calories, protein, fat, carbs, mass) VALUES" +
            "(:nutritionID, :meal, :name, :calories, :protein, :fat, :carbs, :mass)")
    void create(int nutritionID, String meal, String name, float calories, float protein, float fat, float carbs, float mass);

    @Query("DELETE FROM Food WHERE id = (SELECT MAX(id) FROM Food WHERE meal = :meal)")
    void deleteMealLast(String meal);

    @Query("DELETE FROM Food WHERE id = :id")
    void delete_id(int id);

    @Query("DELETE FROM Food WHERE nutritionID = :nutritionID")
    void delete_nutritionID(int nutritionID);

    @Query("SELECT calories FROM Food WHERE nutritionID  = :nutritionID")
    List<Float> getCalories(int nutritionID);

    @Query("SELECT * FROM Food WHERE nutritionID = :nutritionID")
    List<Food> getAll(int nutritionID);

    @Query("SELECT protein FROM Food WHERE nutritionId = :nutritionID")
    List<Float> getProteins(int nutritionID);

    @Query("SELECT fat FROM Food WHERE nutritionId = :nutritionID")
    List<Float> getFats(int nutritionID);

    @Query("SELECT Carbs FROM Food WHERE nutritionId = :nutritionID")
    List<Float> getCarbs(int nutritionID);


    @Query("SELECT * FROM Food WHERE nutritionID = :nutritionID AND meal = :meal")
    List<Food> getMeal(int nutritionID, String meal);
}
