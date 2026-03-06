package com.example.bilal.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bilal.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private boolean isSignInMode = true;
    private boolean isWaitingForOtp = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupAuthToggle();
        setupAuthButton();
        setupSuccessButton();
    }

    private void setupAuthToggle() {
        binding.tvSwitchAuth.setOnClickListener(v -> {
            isSignInMode = !isSignInMode;
            isWaitingForOtp = false;
            updateUI();
        });
    }

    private void updateUI() {
        binding.authScrollView.setVisibility(View.VISIBLE);
        binding.llSuccess.setVisibility(View.GONE);
        binding.tilOtp.setVisibility(isWaitingForOtp ? View.VISIBLE : View.GONE);
        
        if (isSignInMode) {
            binding.tvAuthTitle.setText("Sign In");
            binding.tvAuthSubtitle.setText("Access your premium features");
            binding.btnAuth.setText("Sign In");
            binding.tvForgotPassword.setVisibility(View.VISIBLE);
            binding.tvSwitchAuthPrefix.setText("Don't have an account? ");
            binding.tvSwitchAuth.setText("Sign Up");
            binding.tilEmail.setVisibility(View.VISIBLE);
            binding.tilPassword.setVisibility(View.VISIBLE);
            binding.llSwitchAuth.setVisibility(View.VISIBLE);
        } else {
            if (isWaitingForOtp) {
                binding.tvAuthTitle.setText("Verify Email");
                binding.tvAuthSubtitle.setText("Enter the 4-digit code sent to your email");
                binding.btnAuth.setText("Verify & Register");
                binding.tilEmail.setVisibility(View.GONE);
                binding.tilPassword.setVisibility(View.GONE);
                binding.llSwitchAuth.setVisibility(View.GONE);
            } else {
                binding.tvAuthTitle.setText("Sign Up");
                binding.tvAuthSubtitle.setText("Create a new account");
                binding.btnAuth.setText("Sign Up");
                binding.tilEmail.setVisibility(View.VISIBLE);
                binding.tilPassword.setVisibility(View.VISIBLE);
                binding.llSwitchAuth.setVisibility(View.VISIBLE);
            }
            binding.tvForgotPassword.setVisibility(View.GONE);
            binding.tvSwitchAuthPrefix.setText("Already have an account? ");
            binding.tvSwitchAuth.setText("Sign In");
        }
    }

    private void setupAuthButton() {
        binding.btnAuth.setOnClickListener(v -> {
            if (isSignInMode) {
                handleSignIn();
            } else {
                if (isWaitingForOtp) {
                    handleOtpVerification();
                } else {
                    handleSignUp();
                }
            }
        });

        binding.tvForgotPassword.setOnClickListener(v -> 
            Toast.makeText(getContext(), "Reset password link sent to " + binding.etEmail.getText().toString(), Toast.LENGTH_SHORT).show());
    }

    private void setupSuccessButton() {
        binding.btnContinueToLogin.setOnClickListener(v -> {
            isSignInMode = true;
            isWaitingForOtp = false;
            updateUI();
        });
    }

    private void handleSignIn() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), "Signing in...", Toast.LENGTH_SHORT).show();
    }

    private void handleSignUp() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), "Verification code sent to " + email, Toast.LENGTH_LONG).show();
        isWaitingForOtp = true;
        updateUI();
    }

    private void handleOtpVerification() {
        String otp = binding.etOtp.getText().toString();
        if (otp.length() < 4) {
            Toast.makeText(getContext(), "Please enter a valid 4-digit code", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show Success/Profit View
        binding.authScrollView.setVisibility(View.GONE);
        binding.llSuccess.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
