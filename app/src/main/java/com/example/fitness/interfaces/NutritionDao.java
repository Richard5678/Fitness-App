package com.example.fitness.interfaces;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitness.models.Nutrition;

import java.util.List;

@Dao
public interface NutritionDao {
    @Query("INSERT INTO Nutrition (username, contents, date, year, month, day) VALUES " +
            "(:username, 'Additional Information', :date, :year, :month, :day)")
    void create(String username, String date, int year, int month, int day);

    @Query("SELECT * FROM Nutrition WHERE username = :username")
    List<Nutrition> getAllNutrition(String username);

    @Query("UPDATE Nutrition SET contents = :contents, date = :date WHERE id = :id")
    void save(String contents, String date, int id);

    @Query("DELETE FROM Nutrition WHERE id = :id")
    void delete(int id);

    @Query("UPDATE Nutrition SET year = :year, month = :month, day = :day WHERE id = :NutritionID")
    void updateDateInt(int NutritionID, int year, int month, int day);

    @Query("DELETE FROM Nutrition WHERE id = (SELECT MAX(id) FROM Nutrition)")
    void deleteLast();

    @Query("SELECT MAX(id) FROM Nutrition")
    int getLastID();

    @Query("SELECT * FROM Nutrition WHERE username = :username ORDER BY year DESC, month DESC, DAY DESC LIMIT 7")
    List<Nutrition> getIDsWeek(String username);

    @Query("SELECT date FROM Nutrition WHERE username = :username")
    List<String> getPickedDates(String username);
}
