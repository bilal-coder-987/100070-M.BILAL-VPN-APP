package com.example.bilal.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import com.example.bilal.data.local.entity.MealPlan;

@Dao
public interface MealPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMealPlans(List<MealPlan> plans);

    @Query("SELECT * FROM meal_plans")
    LiveData<List<MealPlan>> getAllMealPlans();

    @Query("SELECT * FROM meal_plans WHERE dayOfWeek = :day")
    LiveData<MealPlan> getMealPlanForDay(String day);
}