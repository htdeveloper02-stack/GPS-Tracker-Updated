package gps.trackerid.location.ui;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import static gps.trackerid.location.utils.Global.isFastClick;

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
import android.os.Handler;
import android.os.Looper;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gps.trackerid.location.R;
<<<<<<< HEAD
import gps.trackerid.location.adshelper.InterstitialAdManager;
=======
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.databinding.ActivityCurrentlocationBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

public class CurrentLocationAct extends BaseActivity implements OnMapReadyCallback {
    ActivityCurrentlocationBinding currentlocationBinding;
    private GoogleMap googleMap;
    private LocationListener locationListener;

    public CurrentLocationAct currentLocationAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentlocationBinding = ActivityCurrentlocationBinding.inflate(getLayoutInflater());
        setContentView(currentlocationBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        currentLocationAct = this;
        currentlocationBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        currentlocationBinding.realMapView.onCreate(null);
        currentlocationBinding.realMapView.getMapAsync(this);

        clickListeners();
        if (Global.banner_all && Global.isInternetConnected(currentLocationAct)) {
            currentlocationBinding.mRlBanner.setVisibility(VISIBLE);
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
<<<<<<< HEAD
        InterstitialAdManager.showIfReady(
                CurrentLocationAct.this,
                "inter_back",
                () -> {
                    finish();
                }
        );

=======
        loadBAckInterstitialAds(currentLocationAct, new AdsConfig.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
    }

    private void clickListeners() {

        currentlocationBinding.ivCurrentLocation.setOnClickListener(v -> getCurrentLocation());

        currentlocationBinding.ivZoomIn.setOnClickListener(v -> {
            if (googleMap != null)
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        });

        currentlocationBinding.ivZoomOut.setOnClickListener(v -> {

            if (googleMap != null)
                googleMap.animateCamera(CameraUpdateFactory.zoomOut());
        });

        currentlocationBinding.ivMapType.setOnClickListener(v -> {
            mShowPopup(v);
        });
        currentlocationBinding.mIvShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFastClick()) return;

                shareText(currentlocationBinding.mTxtShareAddress.getText().toString());
            }
        });
    }

    private void mShowPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(currentLocationAct, R.style.CustomPopupMenu), v);
        popupMenu.inflate(R.menu.map_menu_item);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intrinsics.checkNotNullParameter(menuItem, "menuItem");
                int itemId = menuItem.getItemId();
                if (itemId == R.id.normal_map) {
                    googleMap.setMapType(1);
                    return true;
                }
                if (itemId == R.id.satellite_map) {
                    googleMap.setMapType(2);
                    return true;
                }
                if (itemId == R.id.terrain_map) {
                    googleMap.setMapType(3);
                    return true;
                }
                if (itemId != R.id.hybrid_map) {
                    return false;
                }
                googleMap.setMapType(4);
                return true;
            }
        });
        popupMenu.show();
    }

    public void shareText(String text) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", "" + text);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, "Share app via"));
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

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Do heavy work here (e.g., fetch location, DB, etc.)

            mainHandler.post(() -> {
                // Update UI, add marker here on main thread
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

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

            });
        });

    }

    private void addMarkerOnCurrentLocation(Location location) {

        Address address;
        if (googleMap == null) return;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (Geocoder.isPresent()) {
            try {
                List<Address> fromLocation = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String addressLine = (fromLocation == null || (address = (Address) CollectionsKt.firstOrNull((List) fromLocation)) == null) ? null : address.getAddressLine(0);
                String str = addressLine;
                if (str != null && !StringsKt.isBlank(str)) {
                    currentlocationBinding.mTxtShareAddress.setText(addressLine);
                }
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
        currentlocationBinding.realMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentlocationBinding.realMapView.onStart();
    }

    @Override
    protected void onPause() {
        currentlocationBinding.realMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        currentlocationBinding.realMapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        currentlocationBinding.realMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        currentlocationBinding.realMapView.onLowMemory();
    }

}