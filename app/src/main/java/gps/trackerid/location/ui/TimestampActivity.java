package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerHomeCollapse;
import static gps.trackerid.location.adshelper.AdsConfig.getInterBack;
import static gps.trackerid.location.adshelper.AdsConfig.getInterHome;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeHome;
import static gps.trackerid.location.adshelper.AdsConfig.getNativePhoneLocator;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeSetting;
import static gps.trackerid.location.utils.Global.IsTaken;
import static gps.trackerid.location.utils.Global.mLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.ads.module.ads.ERainAd;
import com.ads.module.funtion.AdCallback;
import com.ads.module.util.AppConstant;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.ads.Preference;
import gps.trackerid.location.ads.SharedUtils;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.InterstitialAdManager;
import gps.trackerid.location.adshelper.NativeAdManager;
import gps.trackerid.location.database.FirebaseUserHelper;
import gps.trackerid.location.databinding.ActivityTimestampBinding;
import gps.trackerid.location.models.users.CurrentUserUtil;
import gps.trackerid.location.models.users.DataUser;
import gps.trackerid.location.service.LocationUpdateService;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.ui.phonetracker.GPStoolActivity;
import gps.trackerid.location.ui.zoneui.ZoneAlertActivity;
import gps.trackerid.location.utils.CarrierDetector;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.PreferenceUtil;
import gps.trackerid.location.utils.QrHelper;
import gps.trackerid.location.utils.TrackingCodeUtil;

public class TimestampActivity extends BaseActivity {
    ActivityTimestampBinding timestampBinding;
    public static TimestampActivity timestampActivity;

    public static TimestampActivity getInstance() {
        return timestampActivity;
    }

    private int mPos = 1;
    public ArrayList<String> mPermissions;
    ShimmerFrameLayout shimmerAds;
    Preference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timestampBinding = ActivityTimestampBinding.inflate(getLayoutInflater());
        setContentView(timestampBinding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        timestampActivity = this;
        preference = new Preference(timestampActivity);
        if (preference.getBoolean("First")) {
            preference.setBoolean("First", false);
        }

        SharedUtils.INSTANCE.setValue(SharedUtils.OPEN_APP, true);

        CarrierDetector.loadFromAssets(timestampActivity);
        timestampBinding.mIvLiveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("location_page", "Current_Location");
                mPos = 1;
                mCallNextAct();
            }
        });
        timestampBinding.mIvNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(NearByActivity.class);
            }
        });
        timestampBinding.mIvZoneAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(ZoneAlertActivity.class);
            }
        });
        timestampBinding.mIvTrafficAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(TrafficAlertActivity.class);
            }
        });

        timestampBinding.mIvGpsTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(GPStoolActivity.class);
            }
        });
        timestampBinding.mIvAddUSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(AddUserActivity.class);
            }
        });
        timestampBinding.mIvUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(ListUserActivity.class);
            }
        });
        timestampBinding.mIvRealtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndPromptGps(new Runnable() {
                    @Override
                    public void run() {
                        mCheckPermission(true);
                    }
                });
            }
        });
        timestampBinding.mIvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(timestampActivity, SettingActivity.class));
            }
        });
        timestampBinding.mIvPhoneLocator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(PhoneLocator.class);
            }
        });

        mSetValue();
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                BSDExitDialog bottomSheet = new BSDExitDialog();
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
        mCheckPermission(false);
        mLoadNative();

        mPreloadNative();
        mPreloadInter();
    }

    private void mPreloadInter() {
        if (Global.inter_home && Global.isInternetConnected(TimestampActivity.this)) {
            InterstitialAdManager.preload(this, getInterHome(), "inter_home");
        }
        if (Global.inter_back && Global.isInternetConnected(TimestampActivity.this)) {
            InterstitialAdManager.preload(this, getInterBack(), "inter_back");
        }
    }

    private void mPreloadNative() {

        if (Global.native_phone_locator) {
            NativeAdManager.getInstance().preloadNativeAd(TimestampActivity.this, getNativePhoneLocator(), R.layout.layout_native_ad_middle, "native_phone_locator");
        }
        if (Global.native_setting) {
            NativeAdManager.getInstance().preloadNativeAd(TimestampActivity.this, getNativeSetting(), R.layout.layout_native_ad_medium, "native_setting");
        }
    }

    private void mNextCallActivity(Class<?> activityClass) {
        InterstitialAdManager.showIfReady(
                TimestampActivity.this,
                "inter_home",
                () -> {
                    startActivity(new Intent(timestampActivity, activityClass));
                }
        );

    }

    private void mCheckPermission() {
        ArrayList<String> mPermissions = new ArrayList<>();
        if (!IsTaken(timestampActivity, mPermissions, "android.permission.ACCESS_NETWORK_STATE")) {
            mPermissions.add("android.permission.ACCESS_NETWORK_STATE");
        }
        if (!mPermissions.isEmpty()) {
            Dexter.withContext(timestampActivity)
                    .withPermissions(mPermissions).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                mCallPhoneLocator();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(error -> {
                        // we are displaying a toast message for error message.
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }).check();
        } else {
            mCallPhoneLocator();
        }
    }

    private void mCallPhoneLocator() {
        Dexter.withContext(timestampActivity)
                .withPermissions("android.permission.READ_CONTACTS").withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            mNextCallActivity(PhoneLocator.class);
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsContactDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> {
                    // we are displaying a toast message for error message.
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }).check();
    }

    private void showSettingsContactDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(timestampActivity, R.style.AppAlertDialogStyle);

        // below line is the title for our alert dialog.
        builder.setTitle(getResources().getString(R.string.needpermission));

        // below line is our message for our dialog
        builder.setMessage(getResources().getString(R.string.needpermission1));
        builder.setPositiveButton(getResources().getString(R.string.gotosetting), (dialog, which) -> {
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel();
            // below is the intent from which we are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel1), (dialog, which) -> {
            // this method is called when user click on negative button.
            dialog.cancel();
        });
        // below line is used to display our dialog
        builder.show();
    }

    private void mCheckPermission(boolean b) {
        mPermissions = new ArrayList();
        if (!IsTaken(timestampActivity, mPermissions, "android.permission.ACCESS_COARSE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (!IsTaken(timestampActivity, mPermissions, "android.permission.ACCESS_FINE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (!IsTaken(timestampActivity, mPermissions, "android.permission.ACCESS_NETWORK_STATE")) {
            mPermissions.add("android.permission.ACCESS_NETWORK_STATE");
        }
        if (!mPermissions.isEmpty()) {
            Dexter.withContext(timestampActivity)
                    .withPermissions(mPermissions).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                if (b)
                                    mCallRealTime();
                            }
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(error -> {
                        // we are displaying a toast message for error message.
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }).check();
        } else {
            if (b)
                mCallRealTime();
        }
    }

    private void mCallRealTime() {
        mNextCallActivity(RealTimeTrackActivity.class);
    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(timestampActivity, R.style.AppAlertDialogStyle);

        // below line is the title for our alert dialog.
        builder.setTitle(getResources().getString(R.string.needpermission));

        // below line is our message for our dialog
        builder.setMessage(getResources().getString(R.string.needpermission1));
        builder.setPositiveButton(getResources().getString(R.string.gotosetting), (dialog, which) -> {
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel();
            // below is the intent from which we are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel1), (dialog, which) -> {
            // this method is called when user click on negative button.
            dialog.cancel();
        });
        // below line is used to display our dialog
        builder.show();
    }

    private void mSetValue() {
        SharedPreferences prefs = PreferenceUtil.getGpsTrackersPreferences(this);
        String randomRegistrationCode = prefs.getString("randomRegistrationCode", "");

        if (randomRegistrationCode.isEmpty()) {
            // Generate a unique ID for this device
            String phoneUniqueId = TrackingCodeUtil.getPhoneUniqueId(this);

            // Save to SharedPreferences
            prefs.edit()
                    .putString("randomRegistrationCode", phoneUniqueId)
                    .apply();

            // Create a DataUser object
            DataUser dataUser = new DataUser(
                    phoneUniqueId,                   // code
                    QrHelper.getDeviceName(),    // name
                    0.0,                             // latitude
                    0.0,                             // longitude
                    true,                            // isActive
                    true,                            // isRegistered
                    null                             // optional fields
            );

            // Add user to Firebase
            FirebaseUserHelper.INSTANCE.addUser(dataUser);

            // Save as current user locally
            CurrentUserUtil.saveCurrentUser(this, dataUser);
        }
//        Intent intent = new Intent(timestampActivity, (Class<?>) LocationUpdateService.class);
//        if (Build.VERSION.SDK_INT >= 26) {
//            ContextCompat.startForegroundService(timestampActivity, intent);
//        } else {
//            startService(intent);
//        }


    }

    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) return false;

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void mCallNextAct() {
        try {
            switch (mPos) {
                case 1:
                    mCallActivity(CurrentLocationAct.class);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mCallActivity(Class cls) {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isGooglePlayServicesAvailable == 1 || isGooglePlayServicesAvailable == 2) {
            isGooglePlaySerive();
        } else {
            mNextCallActivity(cls);
        }
    }

    public boolean isGooglePlaySerive() {
        int isGooglePlayServicesAvailable = 0;
        AlertDialog.Builder builder = null;
        String str = null;
        CharSequence charSequence;
        boolean z = false;
        try {
            isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Get Google Play services");
            str = null;
        } catch (Exception unused) {
        }
        if (isGooglePlayServicesAvailable == 1) {
            str = "To view the Maps in your device Google Play services required, which are missing from your phone.";
            charSequence = "Download Google Play Services";
        } else if (isGooglePlayServicesAvailable != 2) {
            charSequence = null;
            builder.setMessage(str);
            builder.setNeutralButton(charSequence, new DialogInterface.OnClickListener() { // from class: gps.trackerid.location.gps.LocationTrackerHome1.27

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("market://details?id=%1$s", "com.google.android.gms"))));
                    } catch (Exception unused2) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms")));
                    }
                }
            });
            if (z) {
                builder.show();
            }
            return z;
        } else {
            str = "This app won't run unless you update Google Play services.";
            charSequence = "Update";
        }
        z = true;
        builder.setMessage(str);
        builder.setNeutralButton(charSequence, new DialogInterface.OnClickListener() { // from class: gps.trackerid.location.gps.LocationTrackerHome1.27

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("market://details?id=%1$s", "com.google.android.gms"))));
                } catch (Exception unused2) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms")));
                }
            }
        });
        if (z) {
        }
        return z;
    }

    public boolean checkAndPromptGps(Runnable onClickEnabled) {
        if (onClickEnabled == null) {
            throw new IllegalArgumentException("Activity and onClickEnabled cannot be null");
        }

        // Get LocationManager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            mLog("ActivityUtil", "LocationManager is null");
            return false;
        }

        // Check if GPS is enabled
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGpsEnabled) {
            // GPS is disabled, show alert dialog

            new AlertDialog.Builder(timestampActivity, R.style.AppAlertDialogStyle)
                    .setTitle("GPS is off")
                    .setMessage("Please turn on gps first for using more features")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Open location settings
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            // GPS is enabled, call the callback
            onClickEnabled.run();
        }

        return isGpsEnabled;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timestampActivity = null;
    }

    private void mLoadNative() {
        if (AdsConfig.isShowNative(Global.native_home, timestampBinding.frAds) && Global.isInternetConnected(timestampActivity)) {

            shimmerAds = findViewById(R.id.shimmer_native);

            ERainAd.getInstance().loadNativeAd(this, getNativeHome(), R.layout.layout_native_ad_middle, timestampBinding.frAds, shimmerAds, new AdCallback() {
                @Override
                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                    super.onAdFailedToLoad(i);
                    timestampBinding.frAds.removeAllViews();
                }

                @Override
                public void onAdFailedToShow(@Nullable AdError adError) {
                    super.onAdFailedToShow(adError);
                    timestampBinding.frAds.removeAllViews();
                }
            });

        }
        if (Global.banner_collap_home && Global.isInternetConnected(timestampActivity)) {
            ERainAd.getInstance().loadCollapsibleBanner(this, getBannerHomeCollapse(), AppConstant.CollapsibleGravity.BOTTOM, new AdCallback());
        } else {
            timestampBinding.mRlBanner.setVisibility(GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            if (ProcessLifecycleOwner.get()
                    .getLifecycle()
                    .getCurrentState()
                    .isAtLeast(Lifecycle.State.STARTED)) {

                if (isLocationEnabled()) {
                    // Location is ON
//            startService(new Intent(this, LocationUpdateService.class));
                    if (!isFinishing() && !isDestroyed()) {

                        Intent intent = new Intent(TimestampActivity.this, LocationUpdateService.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            ContextCompat.startForegroundService(TimestampActivity.this, intent);
                        } else {
                            startService(intent);
                        }

                    }
                } else {
                    checkAndPromptGps(new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing() && !isDestroyed()) {
                                Intent intent = new Intent(TimestampActivity.this, LocationUpdateService.class);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    ContextCompat.startForegroundService(TimestampActivity.this, intent);
                                } else {
                                    startService(intent);
                                }
                            }
//                    startService(new Intent(TimestampActivity.this, LocationUpdateService.class));
                        }
                    });
                }
            }


//            // Location is OFF
//            LocationRequest locationRequest = LocationRequest.create()
//                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//            LocationSettingsRequest.Builder builder =
//                    new LocationSettingsRequest.Builder()
//                            .addLocationRequest(locationRequest);
//
//            SettingsClient client = LocationServices.getSettingsClient(this);
//            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
//
//            task.addOnSuccessListener(locationSettingsResponse -> {
//                // Location is ON
//                startService(new Intent(this, LocationUpdateService.class));
//            });
//
//            task.addOnFailureListener(e -> {
//                if (e instanceof ResolvableApiException) {
//                    try {
//                        ((ResolvableApiException) e).startResolutionForResult(this, 1001);
//                    } catch (IntentSender.SendIntentException ignored) {}
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(TimestampActivity.this, LocationUpdateService.class));
    }
}