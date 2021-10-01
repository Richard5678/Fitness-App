package com.example.fitness.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;


@Entity(tableName = "Users")
public class User {
    @PrimaryKey
    @NonNull
    private String name;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "mass")
    private float mass;

    @ColumnInfo(name = "gymInserted")
    private int gymInserted;

    @ColumnInfo(name = "sportInserted")
    private int sportInserted;

    @ColumnInfo(name = "outdoorInserted")
    private int outdoorInserted;

    @ColumnInfo(name = "homeInserted")
    private int homeInserted;

    @ColumnInfo(name = "workInserted")
    private int workInserted;

    @ColumnInfo(name = "foodInserted")
    private int foodInserted;

    @ColumnInfo(name = "massStored")
    private int massStored;

    public User(String name, String password, String email, int gymInserted, int sportInserted, int outdoorInserted, int homeInserted, int workInserted, int foodInserted, int massStored, float mass) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.gymInserted = gymInserted;
        this.sportInserted = sportInserted;
        this.outdoorInserted = outdoorInserted;
        this.homeInserted = homeInserted;
        this.workInserted = workInserted;
        this.foodInserted = foodInserted;
        this.massStored = massStored;
        this.mass = mass;
    }

    public void setGymInserted(int inserted) {
        gymInserted = inserted;
    }

    public void setSportInserted(int inserted) {
        sportInserted = inserted;
    }

    public void setOutdoorInserted(int inserted) {
        outdoorInserted = inserted;
    }

    public void setHomeInserted(int inserted) {
        homeInserted= inserted;
    }

    public void setWorkInserted(int inserted) {
        workInserted = inserted;
    }

    public void setMassStored(int massStored) {  this.massStored = massStored; }

    public void setMass(float mass) { this.mass = mass; }


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail()  { return email; }

    public int getGymInserted() {return gymInserted; }

    public int getSportInserted() {return sportInserted; }

    public  int getOutdoorInserted() {return outdoorInserted; }

    public int getHomeInserted() {return homeInserted; }

    public int getWorkInserted() {return workInserted; }

    public int getFoodInserted() {return foodInserted; }

    public int getMassStored() {return massStored; }

    public float getMass() {return mass;}
}
