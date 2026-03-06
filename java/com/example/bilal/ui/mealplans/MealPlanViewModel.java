package com.example.bilal.ui.mealplans;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.bilal.data.local.entity.MealPlan;
import com.example.bilal.data.repository.MealPlanRepository;
import java.util.List;

public class MealPlanViewModel extends AndroidViewModel {
    private MealPlanRepository repository;
    private LiveData<List<MealPlan>> allMealPlans;

    public MealPlanViewModel(@NonNull Application application) {
        super(application);
        repository = new MealPlanRepository(application);
        allMealPlans = repository.getAllMealPlans();
    }

    public LiveData<List<MealPlan>> getAllMealPlans() {
        return allMealPlans;
    }

    public void insert(List<MealPlan> plans) {
        repository.insert(plans);
    }
}