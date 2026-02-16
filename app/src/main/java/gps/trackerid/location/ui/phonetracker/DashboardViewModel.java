package gps.trackerid.location.ui.phonetracker;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DashboardViewModel extends AndroidViewModel {
    public enum TrackingState {
        IDLE,      // App opened / stopped
        RUNNING,   // GPS tracking running
        PAUSED     // Tracking paused
    }

    private final MutableLiveData<Integer> currentSpeed = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> topSpeed = new MutableLiveData<>(0);
    private final MutableLiveData<String> totalDistance = new MutableLiveData<>("0 km");
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private final MutableLiveData<TrackingState> trackingState =
            new MutableLiveData<>(TrackingState.IDLE);

    public LiveData<TrackingState> getTrackingState() {
        return trackingState;
    }


    private final LocationManager locationManager;

    private Location lastLocation;
    private float distance = 0f;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        locationManager =
                (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
    }

    public LiveData<Integer> getCurrentSpeed() {
        return currentSpeed;
    }

    public LiveData<Integer> getTopSpeed() {
        return topSpeed;
    }

    public LiveData<String> getTotalDistance() {
        return totalDistance;
    }

    public LiveData<Boolean> getLoadingState() {
        return loading;
    }

    // 🔥 START GPS
    @SuppressLint("MissingPermission")
    public void start() {
        loading.postValue(true);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                0,
                locationListener
        );
    }

    public void startTracking() {

        trackingState.setValue(TrackingState.RUNNING);
        loading.setValue(true);
        start(); // start GPS
    }

    public void pauseTracking() {
        trackingState.setValue(TrackingState.PAUSED);
        loading.setValue(false);
    }

    public void stopTracking() {
        trackingState.setValue(TrackingState.IDLE);
        currentSpeed.setValue(0);
        loading.setValue(true);
        distance = 0;
        totalDistance.setValue("0 km");
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            loading.postValue(false); // ✅ GPS FIX FOUND

            // SPEED
            if (location.hasSpeed()) {
                int speedKmh = (int) (location.getSpeed() * 3.6f);
                currentSpeed.postValue(speedKmh);

                Integer top = topSpeed.getValue();
                if (top == null || speedKmh > top) {
                    topSpeed.postValue(speedKmh);
                }
            }

            // DISTANCE
            if (lastLocation != null) {
                distance += lastLocation.distanceTo(location);
                totalDistance.postValue(String.format("%.2f km", distance / 1000f));
            }

            lastLocation = location;
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            loading.postValue(true);
        }
    };

    @Override
    protected void onCleared() {
        locationManager.removeUpdates(locationListener);
    }
}

