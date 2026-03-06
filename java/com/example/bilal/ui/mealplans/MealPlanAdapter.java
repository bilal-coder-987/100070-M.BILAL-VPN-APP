package com.example.bilal.ui.mealplans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bilal.R;
import com.example.bilal.data.local.entity.MealPlan;
import java.util.ArrayList;
import java.util.List;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.MealPlanViewHolder> {

    private List<MealPlan> mealPlans = new ArrayList<>();

    public void setMealPlans(List<MealPlan> mealPlans) {
        this.mealPlans = mealPlans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealPlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meal_plan, parent, false);
        return new MealPlanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealPlanViewHolder holder, int position) {
        MealPlan current = mealPlans.get(position);
        holder.tvDay.setText(current.dayOfWeek);
        holder.tvBreakfast.setText("Breakfast: " + current.breakfast);
        holder.tvLunch.setText("Lunch: " + current.lunch);
        holder.tvDinner.setText("Dinner: " + current.dinner);
    }

    @Override
    public int getItemCount() {
        return mealPlans.size();
    }

    class MealPlanViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDay;
        private TextView tvBreakfast;
        private TextView tvLunch;
        private TextView tvDinner;

        public MealPlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvBreakfast = itemView.findViewById(R.id.tv_breakfast);
            tvLunch = itemView.findViewById(R.id.tv_lunch);
            tvDinner = itemView.findViewById(R.id.tv_dinner);
        }
    }
}