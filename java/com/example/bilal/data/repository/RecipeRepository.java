package com.example.bilal.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.bilal.data.local.AppDatabase;
import com.example.bilal.data.local.dao.RecipeDao;
import com.example.bilal.data.local.entity.Recipe;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private LiveData<List<Recipe>> allRecipes;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public RecipeRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
        allRecipes = recipeDao.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public LiveData<List<Recipe>> getFavoriteRecipes() {
        return recipeDao.getFavoriteRecipes();
    }

    public void insert(List<Recipe> recipes) {
        executorService.execute(() -> recipeDao.insertRecipes(recipes));
    }
}