package gps.trackerid.location.ui;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
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
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ads.module.ads.ERainAd;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gps.trackerid.location.R;

import gps.trackerid.location.adshelper.InterstitialAdManager;
import gps.trackerid.location.database.DataUserDao;
import gps.trackerid.location.database.FirebaseUserHelper;
import gps.trackerid.location.databinding.ActivityRealtimetrackBinding;
import gps.trackerid.location.models.users.DataUser;
import gps.trackerid.location.models.users.DataUserViewModelFactory;
import gps.trackerid.location.models.users.USerListViewModel;
import gps.trackerid.location.models.zonedata.AppDatabase;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;
import kotlin.Deprecated;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

public class RealTimeTrackActivity extends BaseActivity implements OnMapReadyCallback {
    ActivityRealtimetrackBinding binding;
    public static RealTimeTrackActivity realTimeTrackActivity;

    public static RealTimeTrackActivity getInstance() {
        return realTimeTrackActivity;
    }

    private GoogleMap googleMap;
    private LocationListener locationListener;
    USerListViewModel uSerListViewModel;
    private boolean isMoveCameraDone;
    private int selectedUserIndex;
    private List<DataUser> userList;
    private Map<String, Marker> userMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRealtimetrackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        realTimeTrackActivity = this;
        DataUserDao dao = AppDatabase.getInstance(this).dataUserDao();
        uSerListViewModel = new ViewModelProvider(
                this,
                new DataUserViewModelFactory(dao)
        ).get(USerListViewModel.class);
        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        binding.mIvMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowPopup(view);
            }
        });
        binding.mIvAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BSDUserDialog bottomSheet = BSDUserDialog.newInstance(userList, new BSDUserDialog.OnUserSelectedListener() {
                    @Override
                    public void onUserSelected(DataUser user) {
                        ShowUserOnmap(user);
                    }
                }, new BSDUserDialog.OnAddUserListener() {
                    @Override
                    public void onAddUser() {

                        InterstitialAdManager.showIfReady(
                                RealTimeTrackActivity.this,
                                "inter_home",
                                () -> {
                                    Intent intent = new Intent(realTimeTrackActivity, AddUserActivity.class);
                                    intent.putExtra("type", "type");
                                    startActivity(intent);
                                }
                        );

                    }
                });
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
        userList = new ArrayList<>();
        this.selectedUserIndex = -1;
        this.userMarkers = new LinkedHashMap();
        setupMap();

//        uSerListViewModel.getAllUserss();
        getUsers();
        if (Global.banner_all && Global.isInternetConnected(realTimeTrackActivity)) {
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

        InterstitialAdManager.showIfReady(
                RealTimeTrackActivity.this,
                "inter_back",
                () -> {
                    finish();
                }
        );

    }

    private void ShowUserOnmap(DataUser dataUser) {
        FirebaseUserHelper.INSTANCE.stopListening();
        FirebaseUserHelper.INSTANCE.startListeningUsers(dataUser.getCode());
        selectedUserIndex = userList.indexOf(dataUser);
        if (googleMap == null) {
            return;
        }
        addUserMarkersToMap(CollectionsKt.listOf(dataUser));
        moveCameraToUser(dataUser.getCode(), 0.0f, 4);
    }

    private void moveCameraToUser(String code, float f, int i) {
        if ((i & 4) != 0) {
            f = 15.0f;
        }
        Marker marker = this.userMarkers.get(code);
        if (marker != null) {
            LatLng position = marker.getPosition();
            Intrinsics.checkNotNullExpressionValue(position, "getPosition(...)");
            marker.showInfoWindow();
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, f));
            return;
        }
    }

    private void getUsers() {
        uSerListViewModel.getAllUsers().observe(this, users -> {
            if (users == null) return;

            List<DataUser> selectedUsers = new ArrayList<>();

            for (DataUser user : users) {
                Log.d("FirebaseUserHelper====", "user.isSelected() = " + user.isSelected());
                if (user.isSelected()) {
                    selectedUsers.add(user);
                }
            }

            userList = selectedUsers;

            if (!userList.isEmpty()) {
                selectedUserIndex = 0;
            }

            if (googleMap != null) {
                addUserMarkersToMap(userList);
            }

            // Start Firebase real-time listening
            try {
                Log.d("FirebaseUserHelper", "selectedUserIndex = " + selectedUserIndex);

                if (selectedUserIndex != -1 && !userList.isEmpty()) {
                    FirebaseUserHelper.INSTANCE
                            .startListeningUsers(userList.get(selectedUserIndex).getCode());
                }
            } catch (DatabaseException e) {
                mLog("RealTimeTracking", "Failed to start listening users"+e.getMessage());
            }
        });

        // Observe Firebase live users (real-time updates)
        FirebaseUserHelper.INSTANCE
                .getUsersLiveData()
                .observe(this, users -> {
                    if (googleMap != null && users != null) {
                        addUserMarkersToMap(users);
                    }
                });
    }

    private void setupMap() {
        binding.realMapView.onCreate(null);
        binding.realMapView.getMapAsync(this);
    }

    private void mShowPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(realTimeTrackActivity, R.style.CustomPopupMenu), v);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        getCurrentLocation();
        addUserMarkersToMap(this.userList);
    }

    public void addUserMarkersToMap(List<DataUser> users) {
        for (DataUser dataUser : users) {
            if (dataUser.isSelected()) {
                double latitude = dataUser.getLatitude();
                double longitude = dataUser.getLongitude();
                if (latitude != 0.0d || longitude != 0.0d) {
                    LatLng latLng = new LatLng(latitude, longitude);
                    Marker marker = this.userMarkers.get(dataUser.getCode());
                    if (marker == null) {
                        MarkerOptions markerOptionsIcon = new MarkerOptions().position(latLng).icon(getMarkerBitmap(R.drawable.ic_marker_add));
                        String name = dataUser.getName();
                        if (name.isEmpty()) {
                            name = "Unknown";
                        }
                        MarkerOptions markerOptionsSnippet = markerOptionsIcon.title(name).snippet(dataUser.getCode());
                        Marker markerAddMarker = googleMap.addMarker(markerOptionsSnippet);
                        if (markerAddMarker != null) {
                            markerAddMarker.showInfoWindow();
                        }
                        if (markerAddMarker != null) {
                            this.userMarkers.put(dataUser.getCode(), markerAddMarker);
                        }
                    } else {
                        Log.d("FirebaseUserHelper", "---------------newPosition " + latLng);
                        if (!Intrinsics.areEqual(marker.getPosition(), latLng)) {
                            marker.setPosition(latLng);
                        }
                    }
                    if (!this.isMoveCameraDone) {
                        this.isMoveCameraDone = true;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
                    }
                }
            }
        }
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
            }
        });
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
        if (locationManager.isProviderEnabled("gps")) {
            Location lastKnownLocation = locationManager.getLastKnownLocation("network");
            if (lastKnownLocation != null) {
                addMarkerOnCurrentLocation(lastKnownLocation);
            }
            locationManager.requestLocationUpdates("network", 1000L, 10.0f, new LocationListener() { // from class: com.live.location.gpsTracker.presentation.activity.RealTimeTrackingActivity.getCurrentLocation.1
                @Override
                public void onLocationChanged(Location location) {
                    Intrinsics.checkNotNullParameter(location, "location");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Intrinsics.checkNotNullParameter(provider, "provider");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Intrinsics.checkNotNullParameter(provider, "provider");
                }

                @Override
                @Deprecated(message = "Deprecated in API 29")
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            });

        }
    }

    private void addMarkerOnCurrentLocation(Location location) {
        Address address;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        if (Geocoder.isPresent()) {
            try {
                List<Address> fromLocation = new Geocoder(this).getFromLocation(latitude, longitude, 1);
                if (fromLocation != null && (address = (Address) CollectionsKt.firstOrNull((List) fromLocation)) != null) {
                    address.getAddressLine(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (googleMap != null) {
            googleMap.clear();
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    }

    private BitmapDescriptor getMarkerBitmap(int vectorResId) {
        Drawable drawable = ContextCompat.getDrawable(this, vectorResId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        drawable.draw(new Canvas(bitmapCreateBitmap));
        BitmapDescriptor bitmapDescriptorFromBitmap = BitmapDescriptorFactory.fromBitmap(bitmapCreateBitmap);
        return bitmapDescriptorFromBitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.realMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.realMapView.onStart();
    }

    @Override
    protected void onPause() {
        binding.realMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        binding.realMapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        binding.realMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.realMapView.onLowMemory();
    }
}