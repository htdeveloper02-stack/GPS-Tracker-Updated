package gps.trackerid.location.models;

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


public class SpeedViewModel extends AndroidViewModel {

    private final MutableLiveData<Float> speed = new MutableLiveData<>(0f);
    private final MutableLiveData<Boolean> gpsReady = new MutableLiveData<>(false);

    private final LocationManager locationManager;

    public SpeedViewModel(@NonNull Application app) {
        super(app);
        locationManager = (LocationManager)
                app.getSystemService(Context.LOCATION_SERVICE);
    }

    public LiveData<Float> getSpeed() {
        return speed;
    }

    public LiveData<Boolean> isGpsReady() {
        return gpsReady;
    }

    @SuppressLint("MissingPermission")
    public void start() {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                0,
                listener
        );
    }

    private final LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            gpsReady.postValue(true);

            if (location.hasSpeed()) {
                float kmh = location.getSpeed() * 3.6f;
                speed.postValue(kmh);
            }
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            gpsReady.postValue(false);
        }
    };

    @Override
    protected void onCleared() {
        locationManager.removeUpdates(listener);
    }
}

