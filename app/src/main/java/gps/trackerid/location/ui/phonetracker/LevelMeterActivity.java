package gps.trackerid.location.ui.phonetracker;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.OnBackPressedCallback;

import com.ads.module.ads.ERainAd;

import gps.trackerid.location.databinding.ActivityLevelmeterBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class LevelMeterActivity extends BaseActivity implements SensorEventListener {
    ActivityLevelmeterBinding binding;
    LevelMeterActivity levelMeterActivity;
    private final float[] gravity = new float[3];

    // smoothing (0.05–0.2)
    private static final float ALPHA = 0.1f;

    // movement scale
    private float moveX = 120f;
    private float moveY = 120f;

    // level tolerance
    private static final float LEVEL_EPSILON = 0.2f;
    private SensorManager sensorManager;
    private Sensor gravitySensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelmeterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        levelMeterActivity = this;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        if (Global.banner_all && Global.isInternetConnected(levelMeterActivity)) {
            binding.mRlBanner.setVisibility(VISIBLE);
            ERainAd.getInstance().loadBanner(this, getBannerAll());
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }

    private void onBackCall() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gravitySensor != null) {
            sensorManager.registerListener(
                    this, gravitySensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        binding.levelDot.clearAnimation();
        binding.levelIndicator.clearAnimation();
    }

    private static final long ANIM_DURATION = 80; // ms


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null || event.sensor == null) return;
        if (event.sensor.getType() != Sensor.TYPE_GRAVITY) return;

        // Low-pass filter (smooth gravity)
        gravity[0] = ALPHA * event.values[0] + (1 - ALPHA) * gravity[0];
        gravity[1] = ALPHA * event.values[1] + (1 - ALPHA) * gravity[1];
        gravity[2] = ALPHA * event.values[2] + (1 - ALPHA) * gravity[2];

        // Calculate tilt angles
        float angleX = (float) Math.toDegrees(
                Math.atan2(gravity[0],
                        Math.sqrt(gravity[1] * gravity[1] + gravity[2] * gravity[2]))
        );

        float angleY = (float) Math.toDegrees(
                Math.atan2(gravity[1],
                        Math.sqrt(gravity[0] * gravity[0] + gravity[2] * gravity[2]))
        );

        // Move indicator
        float tx = gravity[0] * moveX;
        float ty = -gravity[1] * moveY;

        // Clamp movement inside container
        tx = clamp(tx,
                -binding.indicatorBoundingBox.getWidth() / 2f,
                binding.indicatorBoundingBox.getWidth() / 2f);

        ty = clamp(ty,
                -binding.indicatorBoundingBox.getHeight() / 2f,
                binding.indicatorBoundingBox.getHeight() / 2f);

        binding.levelIndicator.setTranslationX(tx);
        binding.levelIndicator.setTranslationY(ty);

        // Dot follows indicator (scaled)
        binding.levelDot.setTranslationX(tx * -0.3f);
        binding.levelDot.setTranslationY(ty * -0.3f);

        // Level detection
        boolean isLevel =
                Math.abs(gravity[0]) < LEVEL_EPSILON &&
                        Math.abs(gravity[1]) < LEVEL_EPSILON;

        binding.levelDot.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor(isLevel ? "#37A4CF" : "#BF4848")
                )
        );

        // Text
        binding.levelX.setText(Html.fromHtml(
                "<b>X:</b> " + Math.abs(round(angleX)) + "°",
                Html.FROM_HTML_MODE_LEGACY));

        binding.levelY.setText(Html.fromHtml(
                "<b>Y:</b> " + Math.abs(round(angleY)) + "°",
                Html.FROM_HTML_MODE_LEGACY));
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private float round(float v) {
        return Math.round(v * 100f) / 100f;
    }

    public static double roundToTwoDecimals(double d) {
        try {
            return Math.round(d * 100.0) / 100.0;
        } catch (IllegalArgumentException e) {
            return Double.NaN;
        }
    }

    private void animateView(View view, float x, float y) {
        view.animate()
                .translationX(x)
                .translationY(y)
                .setDuration(ANIM_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        levelMeterActivity = null;
    }
}