package com.example.bilal.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bilal.data.local.entity.Recipe;
import com.example.bilal.databinding.FragmentRecipeBinding;
import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {

    private FragmentRecipeBinding binding;
    private RecipeViewModel recipeViewModel;
    private RecipeAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        adapter = new RecipeAdapter();
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRecipes.setAdapter(adapter);

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes == null || recipes.isEmpty()) {
                // For demo purposes, insert some mock data if empty
                insertMockRecipes();
            } else {
                adapter.setRecipes(recipes);
            }
        });
    }

    private void insertMockRecipes() {
        List<Recipe> mockList = new ArrayList<>();
        mockList.add(new Recipe("Quinoa Salad", "Healthy salad with quinoa and vegetables", "Quinoa, Cucumber, Tomato, Lemon", "Mix all ingredients and serve cold.", 350));
        mockList.add(new Recipe("Grilled Chicken", "Lean protein with herbs", "Chicken breast, Rosemary, Olive oil", "Grill until golden brown.", 450));
        mockList.add(new Recipe("Avocado Toast", "Simple and nutritious breakfast", "Whole grain bread, Avocado, Egg", "Toast bread, mash avocado, add poached egg.", 300));
        recipeViewModel.insert(mockList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}