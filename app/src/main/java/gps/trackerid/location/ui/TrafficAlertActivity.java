package gps.trackerid.location.ui;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ads.module.ads.ERainAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.InterstitialAdManager;
import gps.trackerid.location.databinding.ActivityTrafficalertBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;
import kotlin.collections.CollectionsKt;

public class TrafficAlertActivity extends BaseActivity implements OnMapReadyCallback {
    ActivityTrafficalertBinding trafficalertBinding;
    private GoogleMap googleMap;
    private LocationListener locationListener;
    private boolean isShortcut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trafficalertBinding = ActivityTrafficalertBinding.inflate(getLayoutInflater());
        setContentView(trafficalertBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        handleShortcutIntent(getIntent());

        trafficalertBinding.realMapView.onCreate(null);
        trafficalertBinding.realMapView.getMapAsync(this);
        trafficalertBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        trafficalertBinding.ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });
        if (Global.banner_all && Global.isInternetConnected(TrafficAlertActivity.this)) {
            trafficalertBinding.mRlBanner.setVisibility(VISIBLE);
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
        InterstitialAdManager.showIfReady(
                TrafficAlertActivity.this,
                "inter_back",
                () -> {
                    if (isShortcut) {
                        startActivity(new Intent(TrafficAlertActivity.this, TimestampActivity.class));
                        finish();
                    } else {
                        finish();
                    }
                }
        );
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

//        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        getCurrentLocation();
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) return;

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            // Get last known location
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                addMarkerOnCurrentLocation(location);
            }

            // Location updates
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
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
                List<Address> fromLocation = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String addressLine = (fromLocation == null || (address = (Address) CollectionsKt.firstOrNull((List) fromLocation)) == null) ? null : address.getAddressLine(0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (googleMap != null) {
            googleMap.setTrafficEnabled(true);
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Current Location").icon(getMarkerBitmap(R.drawable.ic_marker_add)));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
    }

    private final BitmapDescriptor getMarkerBitmap(int vectorResId) {
        Drawable drawable = ContextCompat.getDrawable(this, vectorResId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        drawable.draw(new Canvas(bitmapCreateBitmap));
        BitmapDescriptor bitmapDescriptorFromBitmap = BitmapDescriptorFactory.fromBitmap(bitmapCreateBitmap);
        return bitmapDescriptorFromBitmap;
    }

    // MapView lifecycle
    @Override
    protected void onResume() {
        super.onResume();
        trafficalertBinding.realMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        trafficalertBinding.realMapView.onStart();
    }

    @Override
    protected void onPause() {
        trafficalertBinding.realMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        trafficalertBinding.realMapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        trafficalertBinding.realMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        trafficalertBinding.realMapView.onLowMemory();
    }

    private void handleShortcutIntent(Intent intent) {
        if (intent != null && "android.intent.action.SHORTCUT_TRAFFIC_ALERT".equals(intent.getAction())) {
            // Open the VPN Server screen
            isShortcut = true;
            setRemoteConfigListener(new RemoteConfigListener() {
                @Override
                public void onRemoteConfigLoaded() {
                    if (Global.banner_all && Global.isInternetConnected(TrafficAlertActivity.this)) {
                        trafficalertBinding.mRlBanner.setVisibility(VISIBLE);
                        ERainAd.getInstance().loadBanner(TrafficAlertActivity.this, getBannerAll());
                    }
                }
            });
        } else {
            isShortcut = false;
        }

    }
}