package gps.trackerid.location.ui.compassui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import androidx.fragment.app.Fragment;

import gps.trackerid.location.databinding.FragmentStandardBinding;
import gps.trackerid.location.ui.baseui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StandardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StandardFragment extends BaseFragment implements SensorEventListener {
    FragmentStandardBinding standardBinding;
    private float mDegree = 0.0f;

    private SensorManager msensor;

    public StandardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        standardBinding = FragmentStandardBinding.inflate(getLayoutInflater(), container, false);
        this.msensor = (SensorManager) getActivity().getSystemService("sensor");

        return standardBinding.getRoot();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float round = Math.round(sensorEvent.values[0]);
        standardBinding.tvHeading.setText((int) round +getDirection(round));
        float f = -round;
        RotateAnimation rotateAnimation = new RotateAnimation(this.mDegree, f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(120L);
        rotateAnimation.setFillAfter(true);
        standardBinding.imageViewCompass.startAnimation(rotateAnimation);
        this.mDegree = f;
    }
    private String getDirection(float degree) {
        if (degree >= 337.5 || degree < 22.5) return "° N";
        if (degree >= 22.5 && degree < 67.5) return "° NE";
        if (degree >= 67.5 && degree < 112.5) return "° E";
        if (degree >= 112.5 && degree < 157.5) return "° SE";
        if (degree >= 157.5 && degree < 202.5) return "° S";
        if (degree >= 202.5 && degree < 247.5) return "° SW";
        if (degree >= 247.5 && degree < 292.5) return "° W";
        if (degree >= 292.5 && degree < 337.5) return "° NW";
        return "";
    }
    @Override
    public void onResume() {
        super.onResume();
        this.msensor.registerListener(this, this.msensor.getDefaultSensor(3), 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.msensor.unregisterListener(this);
    }
}