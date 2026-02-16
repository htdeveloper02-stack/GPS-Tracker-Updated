package gps.trackerid.location.ui.phonetracker;

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
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Arrays;
import java.util.Locale;

import gps.trackerid.location.databinding.ActivityLevelmeterBinding;

public class LevelMeterActivity_copy extends AppCompatActivity implements SensorEventListener {
    ActivityLevelmeterBinding binding;
    LevelMeterActivity_copy levelMeterActivity;
    private SensorManager sensorManager;
    private Sensor gravitySensor;

    private final float[] gravity = new float[4];
    private float moveX1, moveY1;

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
        moveX1 = dm.widthPixels / 19.6f;
        moveY1 = dm.heightPixels / 19.6f;

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

    private static final float ALPHA = 0.1f;   // smoothing
    private static final float LEVEL_THRESHOLD = 0.2f;
    private static final long ANIM_DURATION = 80; // ms

/*
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_GRAVITY) return;

        // 🔹 Low-pass filter
        gravity[0] += ALPHA * (event.values[0] - gravity[0]);
        gravity[1] += ALPHA * (event.values[1] - gravity[1]);
        gravity[2] += ALPHA * (event.values[2] - gravity[2]);

        // 🔹 Calculate angles
        float angleX = (float) Math.toDegrees(
                Math.atan2(
                        gravity[0],
                        Math.sqrt(gravity[1] * gravity[1] + gravity[2] * gravity[2])
                )
        );

        float angleY = (float) Math.toDegrees(
                Math.atan2(
                        gravity[1],
                        Math.sqrt(gravity[0] * gravity[0] + gravity[2] * gravity[2])
                )
        );

        // 🔹 Gravity → pixels
        float moveX = gravity[1] / moveX1;
        float moveY = -gravity[0] / moveY1;

        // 🔹 Move indicator
        animateView(binding.levelIndicator, moveX, moveY);

        // 🔹 Dot follows indicator
        animateView(binding.levelDot, moveX * -0.3f, moveY * -0.3f);

        // 🔹 Level detection
        boolean isLevel =
                Math.abs(gravity[0]) < LEVEL_THRESHOLD &&
                        Math.abs(gravity[1]) < LEVEL_THRESHOLD;

        binding.levelDot.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor(isLevel ? "#37A4CF" : "#BF4848")
                )
        );

        // 🔹 Update text
        binding.levelX.setText(
                Html.fromHtml("<b>X:</b> " + Math.abs(Math.round(angleX)) + "°")
        );

        binding.levelY.setText(
                Html.fromHtml("<b>Y:</b> " + Math.abs(Math.round(angleY)) + "°")
        );
    }*/


//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() != Sensor.TYPE_GRAVITY) return;
//
//        gravity[0] = event.values[0];
//        gravity[1] = event.values[1];
//        gravity[2] = event.values[2];
//
//        float angleX = (float) Math.toDegrees(
//                Math.atan2(gravity[0],
//                        Math.sqrt(gravity[1] * gravity[1] + gravity[2] * gravity[2])));
//
//        float angleY = (float) Math.toDegrees(
//                Math.atan2(gravity[1],
//                        Math.sqrt(gravity[0] * gravity[0] + gravity[2] * gravity[2])));
//
//        binding.levelDot.setTranslationX(-gravity[0] * moveX1);
//        binding.levelDot.setTranslationY(gravity[1] * moveY1);
//
//        boolean level =
//                Math.abs(gravity[0]) < 0.2f &&
//                        Math.abs(gravity[1]) < 0.2f;
//
//        binding.levelDot.setColorFilter(Color.parseColor(
//                level ? "#37A4CF" : "#BF4848"));
//
//        binding.levelX.setText(Html.fromHtml(
//                "<b>X:</b> " + Math.abs(Math.round(angleX)) + "°"));
//
//        binding.levelY.setText(Html.fromHtml(
//                "<b>Y:</b> " + Math.abs(Math.round(angleY)) + "°"));
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public final float f41362s = 0.01f;
    public final float[] f41359p = new float[3];

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float f = 0;
        float f2 = 0;
        float f3 = 0;
        float f4 = 0;
        double asin;
        if (sensorEvent.sensor.getType() != Sensor.TYPE_GRAVITY) return;
        float[] values = sensorEvent.values;
        float[] readings = this.f41359p;
        float f5 = values[0];
        float f6 = this.f41362s;
        float f7 = 1 - f6;
        readings[0] = (readings[0] * f7) + (f5 * f6);
        readings[1] = (readings[1] * f7) + (values[1] * f6);
        readings[2] = (f7 * readings[2]) + (f6 * values[2]);
        float degrees = (float) Math.toDegrees((float) Math.atan2(f2, (float) Math.sqrt((f4 * f4) + (f3 * f3))));
        double d = readings[1];
        float f8 = readings[0];
        float f9 = readings[2];
        float[] fArr = {degrees, -((float) Math.toDegrees((float) Math.atan2(d, (float) Math.sqrt((f9 * f9) + (f8 * f8))))), BitmapDescriptorFactory.HUE_RED};
        float[] out = this.gravity;
        double cos = Math.cos(Math.toRadians(fArr[2] / 2.0d));
        double sin = Math.sin(Math.toRadians(fArr[2] / 2.0d));
        double cos2 = Math.cos(Math.toRadians(fArr[1] / 2.0d));
        double sin2 = Math.sin(Math.toRadians(fArr[1] / 2.0d));
        double cos3 = Math.cos(Math.toRadians(fArr[0] / 2.0d));
        double sin3 = Math.sin(Math.toRadians(fArr[0] / 2.0d));
        double d2 = cos3 * cos2;
        double d3 = sin3 * sin2;
        double d4 = sin3 * cos2;
        double d5 = cos3 * sin2;
        out[0] = (float) ((d4 * cos) - (d5 * sin));
        out[1] = (float) ((d4 * sin) + (d5 * cos));
        out[2] = (float) ((d2 * sin) - (d3 * cos));
        out[3] = (float) ((d3 * sin) + (d2 * cos));
        float[] arr = (float[]) out.clone();
//        C15848l.m44526f(arr, "arr");
//        C14477c c14477c2 = new C14477c(arr[0], arr[1], arr[2], arr[3]);
        float f10 = 2;
        float[] fArr2 = arr;
        float f11 = fArr2[3];
        float f12 = fArr2[2];
        float f13 = fArr2[0];
        float f14 = fArr2[1];
        float atan2 = (float) Math.atan2(((f13 * f14) + (f11 * f12)) * f10, f - (((f12 * f12) + (f14 * f14)) * f10));
        float f15 = ((fArr2[3] * fArr2[1]) - (fArr2[2] * fArr2[0])) * f10;
        if (Math.abs(f15) >= 1.0f) {
            asin = Math.copySign(1.5707963267948966d, f15);
        } else {
            asin = Math.asin(f15);
        }
        float f16 = (float) asin;
        float f17 = fArr2[3];
        float f18 = fArr2[0];
        float f19 = fArr2[1];
        float[] fArr3 = {(float) Math.toDegrees((float) Math.atan2(((fArr2[2] * f19) + (f17 * f18)) * f10, f - (((f19 * f19) + (f18 * f18)) * f10))), (float) Math.toDegrees(f16), (float) Math.toDegrees(atan2)};
        float f20 = fArr3[0];
        float f21 = fArr3[1];
        if (-90.0f > f20 || f20 > 90.0f) {
            if (f20 > 90.0f) {
                f20 = 180 - f20;
            } else {
                f20 = -(180 + f20);
            }
        }
        binding.levelDot.setTranslationX(binding.levelIndicator.getTranslationX() * (-0.3f));
        binding.levelDot.setTranslationY(binding.levelIndicator.getTranslationY() * (-0.3f));
        double d6 = readings[0];
        if (-0.2d <= d6 && d6 <= 0.2d) {
            double d7 = readings[1];
            if (-0.2d <= d7 && d7 <= 0.2d) {
                binding.levelDot.setImageTintList(ColorStateList.valueOf(Color.parseColor("#37A4CF")));

                if ((readings[0] * this.moveX1) - ((binding.levelIndicator.getWidth() * 1.0f) / f10) > (binding.indicatorBoundingBox.getWidth() / 2) * (-1) && ((binding.levelIndicator.getWidth() * 1.0f) / f10) + (readings[0] * this.moveX1) < binding.indicatorBoundingBox.getWidth() / 2) {
                    binding.levelIndicator.setTranslationX(readings[0] * this.moveX1);
                }
                float f23 = -1;
                if (((readings[1] * f23) * this.moveY1) - ((binding.levelIndicator.getHeight() * 1.0f) / f10) > (binding.indicatorBoundingBox.getHeight() / 2) * (-1) && ((binding.levelIndicator.getHeight() * 1.0f) / f10) + (readings[1] * f23 * this.moveY1) < binding.indicatorBoundingBox.getHeight() / 2) {
                    binding.levelIndicator.setTranslationY(readings[1] * f23 * this.moveY1);
                }

                String str3 = "<b>X:</b> " + Math.abs(roundToTwoDecimals(f20)) + "°";
                Spanned fromHtml3 = Html.fromHtml(String.format(Locale.getDefault(), "%s", Arrays.copyOf(new Object[]{str3}, 1)), 0);
                binding.levelX.setText(fromHtml3);
                String str4 = "<b>Y:</b> " + Math.abs(roundToTwoDecimals(f21)) + "°";
                Spanned fromHtml4 = Html.fromHtml(String.format(Locale.getDefault(), "%s", Arrays.copyOf(new Object[]{str4}, 1)), 0);
                binding.levelY.setText(fromHtml4);
                return;
            }
        }
        binding.levelDot.setImageTintList(ColorStateList.valueOf(Color.parseColor("#BF4848")));
        if ((readings[0] * this.moveX1) - ((binding.levelIndicator.getWidth() * 1.0f) / f10) > (binding.indicatorBoundingBox.getWidth() / 2) * (-1) && ((binding.levelIndicator.getWidth() * 1.0f) / f10) + (readings[0] * this.moveX1) < binding.indicatorBoundingBox.getWidth() / 2) {
            binding.levelIndicator.setTranslationX(readings[0] * this.moveX1);
        }
        float f23 = -1;
        if (((readings[1] * f23) * this.moveY1) - ((binding.levelIndicator.getHeight() * 1.0f) / f10) > (binding.indicatorBoundingBox.getHeight() / 2) * (-1) && ((binding.levelIndicator.getHeight() * 1.0f) / f10) + (readings[1] * f23 * this.moveY1) < binding.indicatorBoundingBox.getHeight() / 2) {
            binding.levelIndicator.setTranslationY(readings[1] * f23 * this.moveY1);
        }

        String str3 = "<b>X:</b> " + Math.abs(roundToTwoDecimals(f20)) + "°";
        Spanned fromHtml3 = Html.fromHtml(String.format(Locale.getDefault(), "%s", Arrays.copyOf(new Object[]{str3}, 1)), 0);
        binding.levelX.setText(fromHtml3);
        String str4 = "<b>Y:</b> " + Math.abs(roundToTwoDecimals(f21)) + "°";
        Spanned fromHtml4 = Html.fromHtml(String.format(Locale.getDefault(), "%s", Arrays.copyOf(new Object[]{str4}, 1)), 0);
        binding.levelY.setText(fromHtml4);
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