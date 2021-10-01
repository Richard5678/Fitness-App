package com.example.fitness.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "LocalFood")
public class LocalFood {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "username")
    private String username;

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

    @ColumnInfo(name = "quantity")
    private float quantity;

    @ColumnInfo(name = "imageURL")
    private String imageURL;

    public LocalFood(String username, String name, float calories, float protein, float fat, float carbs, float quantity, String imageURL) {
        this.username = username;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.quantity = quantity;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id; }

    public String getUsername() { return username; }

    public String getName() {
        return name;
    }

    public float getCalories() {
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

    public float getQuantity() { return quantity; }

    public String getImageURL() {
        return imageURL;
    }
}
