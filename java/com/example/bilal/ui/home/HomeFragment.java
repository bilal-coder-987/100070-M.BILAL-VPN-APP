package com.example.bilal.ui.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.TrafficStats;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.example.bilal.R;
import com.example.bilal.data.model.Server;
import com.example.bilal.databinding.FragmentHomeBinding;
import com.example.bilal.services.MyVpnService;
import com.example.bilal.ui.VpnViewModel;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private boolean isConnected = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private VpnViewModel viewModel;
    private Server currentServer;
    
    private long startTime = 0;
    private ObjectAnimator waveAnimator;

    // Traffic stats
    private long lastRxBytes = 0;
    private long lastTxBytes = 0;
    private long lastStatsTime = 0;

    private final ActivityResultLauncher<Intent> vpnPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    startVpnService();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    updateUI(false);
                }
            }
    );

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isConnected && binding != null) {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                int hours = minutes / 60;
                seconds = seconds % 60;
                minutes = minutes % 60;
                binding.tvTimer.setText(String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds));
                handler.postDelayed(this, 1000);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(requireActivity()).get(VpnViewModel.class);
        
        setupObservers();
        setupClickListeners();
        setupWaveAnimation();
    }

    private void setupWaveAnimation() {
        if (binding.ivWave == null) return;
        waveAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.ivWave,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0.3f, 0.6f));
        waveAnimator.setDuration(1500);
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator.setRepeatMode(ValueAnimator.REVERSE);
    }

    private void setupObservers() {
        viewModel.getSelectedServer().observe(getViewLifecycleOwner(), server -> {
            if (server != null) {
                currentServer = server;
                binding.tvCountry.setText(server.getCountry());
                binding.tvIp.setText("IP " + server.getIp());
                Glide.with(this).load(server.getFlagUrl()).into(binding.ivFlag);
                
                if (isConnected) {
                    toggleVpn();
                }
            }
        });
    }

    private void setupClickListeners() {
        binding.btnConnect.setOnClickListener(v -> toggleVpn());
        binding.btnPro.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_pro));
    }

    private void toggleVpn() {
        if (!isConnected) {
            Intent intent = VpnService.prepare(getContext());
            if (intent != null) {
                vpnPermissionLauncher.launch(intent);
            } else {
                startVpnService();
            }
        } else {
            stopVpnService();
        }
    }

    private void startVpnService() {
        isConnected = true;
        startTime = System.currentTimeMillis();
        
        // Reset stats
        lastRxBytes = TrafficStats.getTotalRxBytes();
        lastTxBytes = TrafficStats.getTotalTxBytes();
        lastStatsTime = System.currentTimeMillis();
        
        updateUI(true);
        Intent intent = new Intent(getContext(), MyVpnService.class);
        intent.setAction("ACTION_CONNECT");
        requireContext().startService(intent);
        
        handler.post(timerRunnable);
        handler.post(statsRunnable);
        if (waveAnimator != null) waveAnimator.start();
    }

    private void stopVpnService() {
        isConnected = false;
        updateUI(false);
        Intent intent = new Intent(getContext(), MyVpnService.class);
        intent.setAction("ACTION_DISCONNECT");
        requireContext().startService(intent);
        
        handler.removeCallbacks(timerRunnable);
        handler.removeCallbacks(statsRunnable);
        
        if (waveAnimator != null) waveAnimator.cancel();
        if (binding.ivWave != null) {
            binding.ivWave.setScaleX(1f);
            binding.ivWave.setScaleY(1f);
            binding.ivWave.setAlpha(0.3f);
        }
    }

    private void updateUI(boolean connected) {
        if (connected) {
            binding.tvStatus.setText("Connected");
            binding.tvStatus.setTextColor(Color.parseColor("#34C759"));
            binding.ivPower.setColorFilter(Color.parseColor("#D4FF31"));
            binding.tvConnectLabel.setText("Disconnect");
            binding.tvTimer.setVisibility(View.VISIBLE);
            if (binding.ivWave != null) binding.ivWave.setColorFilter(Color.parseColor("#D4FF31"));
        } else {
            binding.tvStatus.setText("Not Connected");
            binding.tvStatus.setTextColor(Color.WHITE);
            binding.ivPower.setColorFilter(Color.parseColor("#8E8E93"));
            binding.tvConnectLabel.setText("Tap to connect");
            binding.tvTimer.setVisibility(View.INVISIBLE);
            binding.tvTimer.setText("00:00:00");
            binding.tvUpload.setText("0 KB/S");
            binding.tvDownload.setText("0 KB/S");
            if (binding.ivWave != null) binding.ivWave.setColorFilter(Color.WHITE);
        }
    }

    private final Runnable statsRunnable = new Runnable() {
        @Override
        public void run() {
            if (isConnected && binding != null) {
                long currentRxBytes = TrafficStats.getTotalRxBytes();
                long currentTxBytes = TrafficStats.getTotalTxBytes();
                long currentTime = System.currentTimeMillis();
                
                long timeDiff = currentTime - lastStatsTime;
                if (timeDiff > 0) {
                    // Bytes to Kilobytes per second
                    double rxSpeed = ((currentRxBytes - lastRxBytes) / 1024.0) / (timeDiff / 1000.0);
                    double txSpeed = ((currentTxBytes - lastTxBytes) / 1024.0) / (timeDiff / 1000.0);
                    
                    // Handle potential reset of TrafficStats
                    if (rxSpeed < 0) rxSpeed = 0;
                    if (txSpeed < 0) txSpeed = 0;

                    binding.tvDownload.setText(formatSpeed(rxSpeed));
                    binding.tvUpload.setText(formatSpeed(txSpeed));
                }
                
                lastRxBytes = currentRxBytes;
                lastTxBytes = currentTxBytes;
                lastStatsTime = currentTime;
                
                handler.postDelayed(this, 1000);
            }
        }
    };

    private String formatSpeed(double speedKbps) {
        if (speedKbps >= 1024) {
            return String.format(Locale.US, "%.1f MB/S", speedKbps / 1024.0);
        } else {
            return String.format(Locale.US, "%.1f KB/S", speedKbps);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        if (waveAnimator != null) waveAnimator.cancel();
        binding = null;
    }
}