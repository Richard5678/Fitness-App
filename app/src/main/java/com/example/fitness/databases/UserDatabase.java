package com.example.fitness.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitness.interfaces.UserDao;
import com.example.fitness.models.User;

@Database(entities = {User.class}, version = 11)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
