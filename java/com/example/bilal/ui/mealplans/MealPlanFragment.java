package com.example.bilal.ui.mealplans;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bilal.data.local.entity.MealPlan;
import com.example.bilal.databinding.FragmentMealPlanBinding;
import java.util.ArrayList;
import java.util.List;

public class MealPlanFragment extends Fragment {

    private FragmentMealPlanBinding binding;
    private MealPlanViewModel mealPlanViewModel;
    private MealPlanAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMealPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MealPlanAdapter();
        binding.rvMealPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMealPlans.setAdapter(adapter);

        mealPlanViewModel = new ViewModelProvider(this).get(MealPlanViewModel.class);
        mealPlanViewModel.getAllMealPlans().observe(getViewLifecycleOwner(), plans -> {
            if (plans == null || plans.isEmpty()) {
                insertMockMealPlans();
            } else {
                adapter.setMealPlans(plans);
            }
        });
    }

    private void insertMockMealPlans() {
        List<MealPlan> mockPlans = new ArrayList<>();
        mockPlans.add(new MealPlan("Monday", "Oatmeal with Blueberries", "Grilled Chicken Salad", "Baked Salmon with Asparagus", "Almonds", 1800));
        mockPlans.add(new MealPlan("Tuesday", "Greek Yogurt Parfait", "Quinoa Bowl", "Turkey Meatballs with Zucchini Noodles", "Apple with Peanut Butter", 1750));
        mockPlans.add(new MealPlan("Wednesday", "Spinach & Feta Omelet", "Lentil Soup", "Steak with Roasted Vegetables", "Carrot Sticks", 1900));
        mockPlans.add(new MealPlan("Thursday", "Smoothie Bowl", "Chickpea Wrap", "Cod with Brown Rice", "Walnuts", 1850));
        mockPlans.add(new MealPlan("Friday", "Whole Grain Toast with Egg", "Tuna Salad", "Shrimp Stir-fry", "Greek Yogurt", 1700));
        mealPlanViewModel.insert(mockPlans);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}