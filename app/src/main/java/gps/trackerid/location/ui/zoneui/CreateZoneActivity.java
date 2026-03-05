package gps.trackerid.location.ui.zoneui;

import static gps.trackerid.location.utils.Global.mLog;

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
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import gps.trackerid.location.R;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.databinding.ActivityCreatezoneBinding;
import gps.trackerid.location.models.zonedata.DataZone;
import gps.trackerid.location.ui.baseui.BaseActivity;
import kotlin.Deprecated;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

public class CreateZoneActivity extends BaseActivity implements OnMapReadyCallback {
    ActivityCreatezoneBinding createzoneBinding;
    public CreateZoneActivity createZoneActivity;
    private GoogleMap googleMap;
    public DataZone selectedZone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createzoneBinding = ActivityCreatezoneBinding.inflate(getLayoutInflater());
        setContentView(createzoneBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        createZoneActivity = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (Build.VERSION.SDK_INT >= 33) {
                selectedZone = extras.getSerializable("selectedZone", DataZone.class);
            } else {
                Serializable serializable2 = extras.getSerializable("selectedZone");
                selectedZone = serializable2 instanceof DataZone ? (DataZone) serializable2 : null;
            }
        }

        if (selectedZone == null) {
            selectedZone = new DataZone(null, "", true, true, true, "", null, null, null, 65);
        }
        createzoneBinding.realMapView.onCreate(null);
        createzoneBinding.realMapView.getMapAsync(this);
        zoneStatusSelection(selectedZone);
        createzoneBinding.checkOnEnter.setChecked(selectedZone.isAlertOnEnter());
        createzoneBinding.checkOnExit.setChecked(selectedZone.isAlertOnExit());
        Integer zoneRadius = selectedZone.getZoneRadius();
        createzoneBinding.seekZoneRadius.setProgress(zoneRadius != null ? zoneRadius.intValue() : 15);
        createzoneBinding.edtZoneName.setText(selectedZone.getName());

        extracted();
        if (googleMap != null) {
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public final void onMapClick(LatLng latLng) {
                    selectedZone.setLongitude(Double.valueOf(latLng.longitude));
                    selectedZone.setLatitude(Double.valueOf(latLng.latitude));
                }
            });
        }
        createzoneBinding.seekZoneRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar p0) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar p0) {
            }

            @Override
            public void onProgressChanged(SeekBar p0, int p1, boolean p2) {
                selectedZone.setZoneRadius(p0 != null ? Integer.valueOf(p0.getProgress()) : null);
                if (googleMap != null) {
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(p0 != null ? p0.getProgress() : 0.0f));
                }
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });

        AdsManager.INSTANCE.loadBannerAll(this, createzoneBinding.mRlBanner);
    }

    private void onBackCall() {
        finish();
    }

    private void extracted() {
        createzoneBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        createzoneBinding.mIvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (createzoneBinding.edtZoneName.getText().toString().isEmpty()) {
                    selectedZone.setName("Unknown");
                } else {
                    selectedZone.setName(createzoneBinding.edtZoneName.getText().toString());
                }
                Intent intent = new Intent();
                intent.putExtra("selectedZone", selectedZone);
                setResult(-1, intent);
                finish();
            }
        });

        createzoneBinding.ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getCurrentLocation(true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        createzoneBinding.ivMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpenMepPop(view);
            }
        });
        createzoneBinding.mIvSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedZone.setSafe(true);
                zoneStatusSelection(selectedZone);
            }
        });
        createzoneBinding.mIvDanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedZone.setSafe(false);
                zoneStatusSelection(selectedZone);
            }
        });
        createzoneBinding.checkOnEnter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                selectedZone.setAlertOnEnter(z);
            }
        });

        createzoneBinding.checkOnExit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                selectedZone.setAlertOnExit(z);
            }
        });
        createzoneBinding.ivSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLocation(createzoneBinding.edtSearchLocation.getText().toString());
            }
        });
        createzoneBinding.edtSearchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 3) {
                    return false;
                }
                searchLocation(createzoneBinding.edtSearchLocation.getText().toString());
                return true;
            }
        });
    }

    private void searchLocation(String query) {
        if (query == null || query.trim().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.enter_location), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Geocoder.isPresent()) {
            Toast.makeText(this, getResources().getString(R.string.not_geo), Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        try {
            // Get the first matching address for the query
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses == null || addresses.isEmpty()) {
                Toast.makeText(this, getResources().getString(R.string.not_location), Toast.LENGTH_SHORT).show();
                return;
            }

            Address address = addresses.get(0);
            double latitude = address.getLatitude();
            double longitude = address.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);

            // Move camera to the searched location
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            } else {
                mLog("searchLocation", "GoogleMap is not initialized");
            }
            String detailedAddress = address.getAddressLine(0);
            createzoneBinding.mTxtAddress.setText(detailedAddress);
            selectedZone.setAddress(detailedAddress);
            selectedZone.setLatitude(Double.valueOf(latitude));
            selectedZone.setLongitude(Double.valueOf(longitude));
            // Log the full address for debugging
            mLog("searchLocation", "Address found: " + address.getAddressLine(0));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.error_location) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void mOpenMepPop(View view) {
        PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(createZoneActivity, R.style.CustomPopupMenu), view);
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

    private void getCurrentLocation(boolean b) throws IOException {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled("gps")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                return;
            }
            Location lastKnownLocation = locationManager.getLastKnownLocation("network");
            if (lastKnownLocation != null) {
                addMarkerOnCurrentLocation(lastKnownLocation, b);
            }
            locationManager.requestLocationUpdates("network", 1000L, 10.0f, new LocationListener() { // from class: com.live.location.gpsTracker.presentation.activity.CreateZoneActivity.getCurrentLocation.1
                @Override // android.location.LocationListener
                public void onLocationChanged(Location location) {
                    Intrinsics.checkNotNullParameter(location, "location");
                }

                @Override // android.location.LocationListener
                public void onProviderDisabled(String provider) {
                    Intrinsics.checkNotNullParameter(provider, "provider");
                }

                @Override // android.location.LocationListener
                public void onProviderEnabled(String provider) {
                    Intrinsics.checkNotNullParameter(provider, "provider");
                }

                @Override // android.location.LocationListener
                @Deprecated(message = "Deprecated in API 29")
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            });
        }
    }

    private void addMarkerOnCurrentLocation(Location location, Boolean clicked) throws IOException {
        Double latitude;
        Double longitude;
        Address address;
        double latitude2 = (Intrinsics.areEqual((Object) clicked, (Object) true) || (latitude = selectedZone.getLatitude()) == null) ? location.getLatitude() : latitude.doubleValue();
        double longitude2 = (Intrinsics.areEqual((Object) clicked, (Object) true) || (longitude = selectedZone.getLongitude()) == null) ? location.getLongitude() : longitude.doubleValue();
        double d = longitude2;
        LatLng latLng = new LatLng(latitude2, d);
        if (Geocoder.isPresent()) {
            try {
                List<Address> fromLocation = new Geocoder(this).getFromLocation(latitude2, d, 1);
                String addressLine = (fromLocation == null || (address = (Address) CollectionsKt.firstOrNull((List) fromLocation)) == null) ? null : address.getAddressLine(0);
                createzoneBinding.mTxtAddress.setText(addressLine);
                if (addressLine == null) {
                    addressLine = "unknown";
                }
                selectedZone.setAddress(addressLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (googleMap != null) {
            googleMap.clear();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, selectedZone.getZoneRadius() != null ? selectedZone.getZoneRadius().floatValue() : 15.0f));
            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public final void onCameraIdle() {
                    LatLng target = googleMap.getCameraPosition().target;
                    String addressFromLatLng = null;
                    try {
                        addressFromLatLng = createZoneActivity.getAddressFromLatLng(target);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    createzoneBinding.mTxtAddress.setText(addressFromLatLng);
                    selectedZone.setAddress(addressFromLatLng);
                    selectedZone.setLatitude(Double.valueOf(target.latitude));
                    selectedZone.setLongitude(Double.valueOf(target.longitude));
                }
            });
        }
    }

    public String getAddressFromLatLng(LatLng latLng) throws IOException {
        try {
            List<Address> fromLocation = new Geocoder(this, Locale.getDefault()).getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (fromLocation != null && !fromLocation.isEmpty()) {
                String addressLine = fromLocation.get(0).getAddressLine(0);
                return addressLine == null ? "Address not found" : addressLine;
            }
            return "No address found";
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to get address";
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

    private void zoneStatusSelection(DataZone selectedZone) {
        if (selectedZone.isSafe()) {
            createzoneBinding.mIvSafe.setBackground(ContextCompat.getDrawable(createZoneActivity, R.drawable.bg_safe_selcted));
            createzoneBinding.mIvDanger.setBackground(ContextCompat.getDrawable(createZoneActivity, R.drawable.bg_safe_unselcted));
            createzoneBinding.ivZoneStatus.setImageResource(R.drawable.ic_zone_safe_marker);
            return;
        }
        createzoneBinding.mIvSafe.setBackground(ContextCompat.getDrawable(createZoneActivity, R.drawable.bg_safe_unselcted));
        createzoneBinding.mIvDanger.setBackground(ContextCompat.getDrawable(createZoneActivity, R.drawable.bg_safe_selcted));
        createzoneBinding.ivZoneStatus.setImageResource(R.drawable.ic_zone_danger_marker);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }

        map.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            getCurrentLocation(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (selectedZone.getLatitude() != null && selectedZone.getLongitude() != null) {
            Double latitude = selectedZone.getLatitude();
            double dDoubleValue = latitude.doubleValue();
            Double longitude = selectedZone.getLongitude();
            LatLng latLng = new LatLng(dDoubleValue, longitude.doubleValue());
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, createzoneBinding.seekZoneRadius.getProgress()));
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        createzoneBinding.realMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        createzoneBinding.realMapView.onStart();
    }

    @Override
    protected void onPause() {
        createzoneBinding.realMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        createzoneBinding.realMapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        createzoneBinding.realMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        createzoneBinding.realMapView.onLowMemory();
    }
}