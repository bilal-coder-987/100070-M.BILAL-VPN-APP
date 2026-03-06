package com.example.bilal.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.bilal.data.local.AppDatabase;
import com.example.bilal.data.local.dao.MealPlanDao;
import com.example.bilal.data.local.entity.MealPlan;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealPlanRepository {
    private MealPlanDao mealPlanDao;
    private LiveData<List<MealPlan>> allMealPlans;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public MealPlanRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mealPlanDao = db.mealPlanDao();
        allMealPlans = mealPlanDao.getAllMealPlans();
    }

    public LiveData<List<MealPlan>> getAllMealPlans() {
        return allMealPlans;
    }

    public LiveData<MealPlan> getMealPlanForDay(String day) {
        return mealPlanDao.getMealPlanForDay(day);
    }

    public void insert(List<MealPlan> plans) {
        executorService.execute(() -> mealPlanDao.insertMealPlans(plans));
    }
}