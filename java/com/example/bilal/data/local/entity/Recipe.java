package com.example.bilal.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public String ingredients;
    public String instructions;
    public int calories;
    public boolean isFavorite;

    public Recipe(String title, String description, String ingredients, String instructions, int calories) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.calories = calories;
        this.isFavorite = false;
    }
}