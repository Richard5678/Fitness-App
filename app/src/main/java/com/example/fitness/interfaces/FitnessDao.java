package com.example.fitness.interfaces;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitness.models.Fitness;

import java.util.List;

@Dao
public interface FitnessDao {
    @Query("INSERT INTO Fitness (username, contents, date, year, month, day) VALUES " +
            "(:username, 'Additional Information', :date, :year, :month, :day)")
    void create(String username, String date, int year, int month, int day);

    @Query("SELECT * FROM Fitness WHERE username = :username")
    List<Fitness> getAllFitness(String username);

    @Query("UPDATE Fitness SET contents = :contents, date = :date WHERE id = :id")
    void save(String contents, String date, int id);

    @Query("DELETE FROM Fitness WHERE id = :id")
    void delete(int id);

    @Query("UPDATE Fitness SET year = :year, month = :month, day = :day WHERE id = :fitnessID")
    void updateDateInt(int fitnessID, int year, int month, int day);

    @Query("DELETE FROM Fitness WHERE id = (SELECT MAX(id) FROM Fitness)")
    void deleteLast();

    @Query("SELECT MAX(id) FROM Fitness")
    int getLastID();

    @Query("SELECT * FROM Fitness WHERE username = :username ORDER BY year DESC, month DESC, DAY DESC LIMIT 7")
    List<Fitness> getIDsWeek(String username);

    @Query("SELECT date FROM Fitness WHERE username = :username")
    List<String> getPickedDates(String username);
}
