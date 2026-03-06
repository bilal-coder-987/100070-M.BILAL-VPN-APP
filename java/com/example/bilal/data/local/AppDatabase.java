package com.example.bilal.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.bilal.data.local.dao.UserDao;
import com.example.bilal.data.local.dao.RecipeDao;
import com.example.bilal.data.local.dao.MealPlanDao;
import com.example.bilal.data.local.entity.User;
import com.example.bilal.data.local.entity.Recipe;
import com.example.bilal.data.local.entity.MealPlan;

@Database(entities = {User.class, Recipe.class, MealPlan.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract RecipeDao recipeDao();
    public abstract MealPlanDao mealPlanDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "diet_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}