package com.example.bilal.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int age;
    public float weight;
    public String goal; // e.g., "Weight Loss", "Muscle Gain"
    public String preference; // e.g., "Vegan", "Vegetarian"
    public int dailyCalorieGoal;

    public User(String name, int age, float weight, String goal, String preference, int dailyCalorieGoal) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.goal = goal;
        this.preference = preference;
        this.dailyCalorieGoal = dailyCalorieGoal;
    }
}