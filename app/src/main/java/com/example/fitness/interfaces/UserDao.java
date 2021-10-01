package com.example.fitness.interfaces;


import androidx.room.Dao;
import androidx.room.Query;

import com.example.fitness.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("INSERT INTO Users (name, password, email, gymInserted, sportInserted, outdoorInserted, homeInserted, workInserted, foodInserted, massStored, mass) " +
            "VALUES (:username, :password, :email, :gymInserted, :sportInserted, :outdoorInserted, :homeInserted, :workInserted, :foodInserted, :massStored, :mass)")
    void create(String username, String password, String email, int gymInserted, int sportInserted, int outdoorInserted, int homeInserted, int workInserted, int foodInserted, int massStored, float mass);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT name FROM users")
    List<String> getUsername();

    @Query("SELECT gymInserted FROM Users WHERE name = :username")
    int getGymInserted(String username);

    @Query("SELECT sportInserted FROM Users WHERE name = :username")
    int getSportInserted(String username);

    @Query("SELECT outdoorInserted FROM Users WHERE name = :username")
    int getOutdoorInserted(String username);

    @Query("SELECT homeInserted FROM Users WHERE name = :username")
    int getHomeInserted(String username);

    @Query("SELECT workInserted FROM Users WHERE name = :username")
    int getWorkInserted(String username);

    @Query("SELECT foodInserted FROM Users WHERE name = :username")
    int getFoodInserted(String username);

    @Query("SELECT massStored FROM Users WHERE name = :username")
    int getMassStored(String username);

    @Query("SELECT mass FROM Users WHERE name = :username")
    int getMass(String username);

    @Query("UPDATE Users SET gymInserted = :inserted WHERE name = :username")
    void setGymInserted(String username, int inserted);

    @Query("UPDATE Users SET sportInserted = :inserted WHERE name = :username")
    void setSportInserted(String username, int inserted);

    @Query("UPDATE Users SET outdoorInserted = :inserted WHERE name = :username")
    void setOutdoorInserted(String username, int inserted);

    @Query("UPDATE Users SET homeInserted = :inserted WHERE name = :username")
    void setHomeInserted(String username, int inserted);

    @Query("UPDATE Users SET workInserted = :inserted WHERE name = :username")
    void setWorkInserted(String username, int inserted);

    @Query("UPDATE Users SET foodInserted = :inserted WHERE name = :username")
    void setFoodInserted(String username, int inserted);

    @Query("UPDATE Users SET massStored = :massStored WHERE name = :username")
    void setMassStored(int massStored, String username);

    @Query("UPDATE Users SET mass = :mass WHERE name = :username")
    void setMass(float mass, String username);

    @Query("UPDATE Users SET password = :password WHERE name = :username")
    void changePassword(String username, String password);
}
