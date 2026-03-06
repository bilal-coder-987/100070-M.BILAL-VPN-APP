package com.example.bilal.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_plans")
public class MealPlan {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String dayOfWeek;
    public String breakfast;
    public String lunch;
    public String dinner;
    public String snack;
    public int totalCalories;

    public MealPlan(String dayOfWeek, String breakfast, String lunch, String dinner, String snack, int totalCalories) {
        this.dayOfWeek = dayOfWeek;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
        this.totalCalories = totalCalories;
    }
}