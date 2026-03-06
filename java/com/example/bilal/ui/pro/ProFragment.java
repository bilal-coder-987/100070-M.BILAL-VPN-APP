package com.example.bilal.ui.pro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.bilal.databinding.FragmentProBinding;

public class ProFragment extends Fragment {

    private FragmentProBinding binding;
    private String selectedPlan = "3 Months";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnBack.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        binding.plan1Month.setOnClickListener(v -> selectPlan("1 Month", binding.plan1Month));
        binding.plan3Months.setOnClickListener(v -> selectPlan("3 Months", binding.plan3Months));
        binding.plan1Year.setOnClickListener(v -> selectPlan("1 Year", binding.plan1Year));

        binding.btnSubscribe.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Redirecting to Google Play for " + selectedPlan + " plan...", Toast.LENGTH_SHORT).show();
            // Simulate Google Play redirect
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + requireContext().getPackageName()));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Google Play Store not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectPlan(String plan, View cardView) {
        selectedPlan = plan;
        
        // Reset all strokes
        binding.plan1Month.setStrokeColor(getResources().getColor(android.R.color.darker_gray));
        binding.plan1Month.setStrokeWidth(2);
        binding.plan3Months.setStrokeColor(getResources().getColor(android.R.color.darker_gray));
        binding.plan3Months.setStrokeWidth(2);
        binding.plan1Year.setStrokeColor(getResources().getColor(android.R.color.darker_gray));
        binding.plan1Year.setStrokeWidth(2);

        // Set selected stroke
        com.google.android.material.card.MaterialCardView selectedView = (com.google.android.material.card.MaterialCardView) cardView;
        selectedView.setStrokeColor(getResources().getColor(com.example.bilal.R.color.vpnAccent));
        selectedView.setStrokeWidth(6);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
