package gps.trackerid.location.ui.phonetracker;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import gps.trackerid.location.R;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.databinding.SpeedometerBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;

public class Speedometer extends BaseActivity {
    SpeedometerBinding speedometerBinding;
    Speedometer speedometer;
    private DashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speedometerBinding = SpeedometerBinding.inflate(getLayoutInflater());
        setContentView(speedometerBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        speedometer = this;
        viewModel = new ViewModelProvider(Speedometer.this)
                .get(DashboardViewModel.class);
        setupUI();
        observeData();
        checkPermission();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });

        AdsManager.INSTANCE.loadBannerAll(this, speedometerBinding.mRlBanner);

    }

    private void onBackCall() {
        finish();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            viewModel.start();

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    101
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int code,
                                           @NonNull String[] p,
                                           @NonNull int[] r) {

        super.onRequestPermissionsResult(code, p, r);
        if (code == 101 && r.length > 0
                && r[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.start();
        }
    }

    private void setupUI() {

        speedometerBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });

        // Start button
        speedometerBinding.startBtn.setOnClickListener(v ->
                viewModel.startTracking()
        );

        // Pause button
        speedometerBinding.pauseBtn.setOnClickListener(v ->
                viewModel.pauseTracking()
        );

        // Stop button
        speedometerBinding.stopBtn.setOnClickListener(v ->
                viewModel.stopTracking()
        );
    }

    private void observeData() {

        // Current speed
        viewModel.getCurrentSpeed().observe(this, speed -> {
            speedometerBinding.arcProgress.setSpeed(speed);
            speedometerBinding.avgSpeedText.setText(String.valueOf(speed));
        });

        // Top speed
        viewModel.getTopSpeed().observe(this, speed ->
                speedometerBinding.topSpeedText.setText(String.valueOf(speed))
        );

        // Total distance
        viewModel.getTotalDistance().observe(this, distance ->
                speedometerBinding.totalDistanceText.setText(distance)
        );

        // Loading state
//        viewModel.getLoadingState().observe(this, loading -> {
//            speedometerBinding.loadingText.setText(getResources().getString(R.string.waiting_for_gps_signal_d, new Object[]{Integer.valueOf(cVar.f7313k)}));
//
//            speedometerBinding.loadingLayout.setVisibility(
//                    loading ? View.VISIBLE : View.GONE
//            );
//        });

        viewModel.getLoadingState().observe(this, loading -> {
            speedometerBinding.loadingText.setText(
                    loading
                            ? getString(R.string.waiting_for_gps_signal_d)
                            : ""
            );

            speedometerBinding.loadingLayout.setVisibility(
                    loading ? View.VISIBLE : View.INVISIBLE
            );
        });
        viewModel.getTrackingState().observe(this, state -> {

            switch (state) {

                case IDLE:
                    speedometerBinding.startBtn.setEnabled(true);
                    speedometerBinding.pauseBtn.setEnabled(false);
                    speedometerBinding.stopBtn.setEnabled(false);
                    break;

                case RUNNING:
                    speedometerBinding.startBtn.setEnabled(false);
                    speedometerBinding.pauseBtn.setEnabled(true);
                    speedometerBinding.stopBtn.setEnabled(true);
                    break;

                case PAUSED:
                    speedometerBinding.startBtn.setEnabled(true);
                    speedometerBinding.pauseBtn.setEnabled(false);
                    speedometerBinding.stopBtn.setEnabled(true);
                    break;
            }
        });
        speedometerBinding.startBtn.setOnClickListener(v ->
                viewModel.startTracking()
        );

        speedometerBinding.pauseBtn.setOnClickListener(v ->
                viewModel.pauseTracking()
        );

        speedometerBinding.stopBtn.setOnClickListener(v ->
                viewModel.stopTracking()
        );

    }


}