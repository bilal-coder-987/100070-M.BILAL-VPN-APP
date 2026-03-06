package com.example.bilal.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bilal.MainActivity;
import com.example.bilal.data.local.entity.User;
import com.example.bilal.data.repository.UserRepository;
import com.example.bilal.databinding.ActivityOnboardingBinding;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository(getApplication());

        binding.btnContinue.setOnClickListener(v -> {
            saveUserAndContinue();
        });
    }

    private void saveUserAndContinue() {
        String name = binding.etName.getText().toString();
        String ageStr = binding.etAge.getText().toString();
        String weightStr = binding.etWeight.getText().toString();

        if (name.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        float weight = Float.parseFloat(weightStr);

        // Default goals for now, can be expanded with more UI
        User user = new User(name, age, weight, "Weight Loss", "General", 2000);
        userRepository.insert(user);

        startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
        finish();
    }
}