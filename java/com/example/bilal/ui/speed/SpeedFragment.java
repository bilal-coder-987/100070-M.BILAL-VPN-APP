package com.example.bilal.ui.speed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bilal.databinding.FragmentSpeedBinding;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpeedFragment extends Fragment {

    private FragmentSpeedBinding binding;
    private boolean isTesting = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSpeedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnTestSpeed.setOnClickListener(v -> {
            if (!isTesting) {
                startRealSpeedTest();
            }
        });
    }

    private void startRealSpeedTest() {
        isTesting = true;
        binding.btnTestSpeed.setEnabled(false);
        binding.btnTestSpeed.setText("Testing...");
        binding.speedGauge.setProgress(0);
        binding.tvSpeedValue.setText("0.0");
        binding.tvPingValue.setText("-- ms");
        binding.tvJitterValue.setText("-- ms");

        executor.execute(() -> {
            try {
                // 1. Measure Ping (Using Google as a reliable target)
                long pingSum = 0;
                int successfulPings = 0;
                long lastPing = 0;
                long jitterSum = 0;

                for (int i = 0; i < 4; i++) {
                    try {
                        long start = System.currentTimeMillis();
                        HttpURLConnection conn = (HttpURLConnection) new URL("https://www.google.com/generate_204").openConnection();
                        conn.setConnectTimeout(3000);
                        conn.setReadTimeout(3000);
                        conn.connect();
                        int responseCode = conn.getResponseCode();
                        long time = System.currentTimeMillis() - start;
                        
                        if (responseCode == 204 || responseCode == 200) {
                            pingSum += time;
                            if (successfulPings > 0) {
                                jitterSum += Math.abs(time - lastPing);
                            }
                            lastPing = time;
                            successfulPings++;
                        }
                        conn.disconnect();
                    } catch (Exception e) {
                        Log.e("SpeedTest", "Ping iteration " + i + " failed", e);
                    }
                    Thread.sleep(100);
                }
                
                if (successfulPings > 0) {
                    long avgPing = pingSum / successfulPings;
                    long avgJitter = successfulPings > 1 ? jitterSum / (successfulPings - 1) : 0;
                    updatePingJitterUI(avgPing, avgJitter);
                } else {
                    updatePingJitterUI(-1, -1);
                }

                // 2. Measure Download Speed
                // Using a reliable large file from a fast CDN (Microsoft or Google)
                String testFileUrl = "https://officecdn-microsoft-com.akamaized.net/pr/492350f6-3a01-4f97-b9c0-c7c6ddf67d60/media/en-us/setupexp.exe";
                URL url = new URL(testFileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestProperty("Accept-Encoding", "identity");
                
                long startTime = System.currentTimeMillis();
                long totalBytesRead = 0;
                
                InputStream inputStream = connection.getInputStream();
                byte[] buffer = new byte[65536]; // 64KB buffer
                int bytesRead;
                
                long maxTestTime = 8000; // 8 seconds test is enough
                
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    totalBytesRead += bytesRead;
                    long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime;
                    
                    if (elapsedTime > 500) { // Start calculating after 0.5s to avoid burst noise
                        double speedMbps = (totalBytesRead * 8.0 / (1024 * 1024)) / (elapsedTime / 1000.0);
                        updateSpeedUI(speedMbps);
                    }

                    if (elapsedTime >= maxTestTime) break;
                    if (!isTesting) break;
                }
                
                try { inputStream.close(); } catch (Exception ignored) {}
                connection.disconnect();
                finishTest();

            } catch (Exception e) {
                Log.e("SpeedTest", "Error during speed test", e);
                handleError(e.getMessage());
            }
        });
    }

    private void updatePingJitterUI(long ping, long jitter) {
        if (getActivity() == null || binding == null) return;
        getActivity().runOnUiThread(() -> {
            if (binding != null) {
                if (ping == -1) {
                    binding.tvPingValue.setText("Fail");
                    binding.tvJitterValue.setText("Fail");
                } else {
                    binding.tvPingValue.setText(ping + " ms");
                    binding.tvJitterValue.setText(jitter + " ms");
                }
            }
        });
    }

    private void updateSpeedUI(double speed) {
        if (getActivity() == null || binding == null) return;
        getActivity().runOnUiThread(() -> {
            if (binding != null) {
                // Smooth the gauge a bit by capping or normalizing if needed
                binding.speedGauge.setProgress((int) Math.min(speed, 100));
                binding.tvSpeedValue.setText(String.format(Locale.US, "%.1f", speed));
            }
        });
    }

    private void handleError(String msg) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            if (binding != null) {
                binding.btnTestSpeed.setText("Error. Try Again");
                binding.btnTestSpeed.setEnabled(true);
                isTesting = false;
                Toast.makeText(getContext(), "Connection Error: Check your internet", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void finishTest() {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            if (binding != null) {
                isTesting = false;
                binding.btnTestSpeed.setEnabled(true);
                binding.btnTestSpeed.setText("Test Again");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isTesting = false;
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}