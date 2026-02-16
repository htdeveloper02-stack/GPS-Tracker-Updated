package gps.trackerid.location.ui.compassui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.databinding.FragmentNormalBinding;
import gps.trackerid.location.ui.baseui.BaseFragment;
import kotlin.collections.CollectionsKt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NormalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NormalFragment extends BaseFragment implements SensorEventListener, OnMapReadyCallback {

    FragmentNormalBinding binding;
    private SensorManager msensor;

    double Latitude = 0.0d;

    double Longitude = 0.0d;

    String string = "Address not found";

    private float mDegree = 0.0f;
    private GoogleMap googleMap;
    private LocationListener locationListener;

    public NormalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNormalBinding.inflate(getLayoutInflater(), container, false);
        this.msensor = (SensorManager) getActivity().getSystemService("sensor");
        binding.realMapView.onCreate(null);
        binding.realMapView.getMapAsync(this);
        binding.ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });
        return binding.getRoot();
    }

    @Override

    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

//        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        getCurrentLocation();
    }

    private void getCurrentLocation() {

        googleMap.setMapType(1);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) return;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            // Get last known location
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                addMarkerOnCurrentLocation(location);
//                showCurrentAddress(location);
            }

            // Location updates
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    addMarkerOnCurrentLocation(location);
//                    showCurrentAddress(location);
                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            };

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,     // 1 second
                    10,       // 10 meters
                    locationListener
            );
        }
    }

    private void addMarkerOnCurrentLocation(Location location) {

        Address address;
        if (googleMap == null) return;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (Geocoder.isPresent()) {
            try {
                List<Address> fromLocation = new Geocoder(getActivity()).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String addressLine = (fromLocation == null || (address = (Address) CollectionsKt.firstOrNull((List) fromLocation)) == null) ? null : address.getAddressLine(0);
                String str = addressLine;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Current Location").icon(getMarkerBitmap(R.drawable.ic_marker_add)));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    }

    private BitmapDescriptor getMarkerBitmap(int vectorResId) {
        if (getActivity() != null && vectorResId != -1) {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), vectorResId);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            drawable.draw(new Canvas(bitmapCreateBitmap));
            BitmapDescriptor bitmapDescriptorFromBitmap = BitmapDescriptorFactory.fromBitmap(bitmapCreateBitmap);
            return bitmapDescriptorFromBitmap;
        } else {
            return null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float f = -Math.round(sensorEvent.values[0]);
        RotateAnimation rotateAnimation = new RotateAnimation(this.mDegree, f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(120L);
        rotateAnimation.setFillAfter(true);
        binding.imageViewCompass.startAnimation(rotateAnimation);
        this.mDegree = f;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.realMapView.onResume();
        this.msensor.registerListener(this, this.msensor.getDefaultSensor(3), 1);
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.realMapView.onPause();
        this.msensor.unregisterListener(this);
    }

    @Override
    public void onStop() {
        binding.realMapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        binding.realMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.realMapView.onLowMemory();
    }
}