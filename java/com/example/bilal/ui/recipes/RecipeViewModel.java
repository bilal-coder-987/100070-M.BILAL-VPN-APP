package com.example.bilal.ui.recipes;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.bilal.data.local.entity.Recipe;
import com.example.bilal.data.repository.RecipeRepository;
import java.util.List;

public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository repository;
    private LiveData<List<Recipe>> allRecipes;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        repository = new RecipeRepository(application);
        allRecipes = repository.getAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    public void insert(List<Recipe> recipes) {
        repository.insert(recipes);
    }
}