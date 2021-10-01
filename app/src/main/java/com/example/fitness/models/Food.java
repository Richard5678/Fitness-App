package com.example.fitness.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Food")
public class Food {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "nutritionID")
    private int nutritionID;

    @ColumnInfo(name = "meal")
    private String meal;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "calories")
    private float calories;

    @ColumnInfo(name = "protein")
    private float protein;

    @ColumnInfo(name = "fat")
    private float fat;

    @ColumnInfo(name = "carbs")
    private float carbs;

    @ColumnInfo(name = "mass")
    private float mass;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Food(int nutritionID, String meal, String name, float calories, float protein, float fat, float carbs, float mass) {
        this.nutritionID = nutritionID;
        this.meal = meal;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.mass = mass;
    }

    public int getNutritionID() {
        return nutritionID;
    }

    public String getMeal() {
        return meal;
    }

    public String getName() {
        return name;
    }

    public float getCalories(){
        return calories;
    }

    public float getProtein() {
        return protein;
    }

    public float getFat() {
        return fat;
    }

    public float getCarbs() {
        return carbs;
    }

    public float getMass() {
        return mass;
    }

}
