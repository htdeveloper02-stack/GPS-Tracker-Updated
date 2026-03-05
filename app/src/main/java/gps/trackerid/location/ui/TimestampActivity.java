package gps.trackerid.location.ui;

import static gps.trackerid.location.ads.AdsManagerKt.isNetwork;
import static gps.trackerid.location.utils.Global.IsTaken;
import static gps.trackerid.location.utils.Global.mLog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.ads.module.admob.AppOpenManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.Preference;
import gps.trackerid.location.ads.RemoteUtils;
import gps.trackerid.location.ads.SharedUtils;
import gps.trackerid.location.database.FirebaseUserHelper;
import gps.trackerid.location.databinding.ActivityTimestampBinding;
import gps.trackerid.location.models.users.CurrentUserUtil;
import gps.trackerid.location.models.users.DataUser;
import gps.trackerid.location.service.LocationUpdateService;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.ui.phonetracker.GPStoolActivity;
import gps.trackerid.location.ui.zoneui.ZoneAlertActivity;
import gps.trackerid.location.utils.CarrierDetector;
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

    @SuppressLint("SourceLockedOrientationActivity")
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

        mLoadAds();
        mPreloadInter();
    }

    private void mPreloadInter() {
        AdsManager.INSTANCE.loadInterBack(this);
        AdsManager.INSTANCE.loadInterHome(this);

        if (RemoteUtils.INSTANCE.getOnOpenResume() && isNetwork(this)) {
            AppOpenManager.getInstance().enableAppResume();
        } else {
            AppOpenManager.getInstance().disableAppResume();
        }
    }

    private void mNextCallActivity(Class<?> activityClass) {
        AdsManager.INSTANCE.showInterHome(TimestampActivity.this, () -> {
            startActivity(new Intent(timestampActivity, activityClass));
            return null;
        });
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
            builder.setNeutralButton(charSequence, (dialogInterface, i) -> {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("market://details?id=%1$s", "com.google.android.gms"))));
                } catch (Exception unused2) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms")));
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
        builder.setNeutralButton(charSequence, (dialogInterface, i) -> {
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("market://details?id=%1$s", "com.google.android.gms"))));
            } catch (Exception unused2) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms")));
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

    private void mLoadAds() {
        AdsManager.INSTANCE.loadNativeHome(this, timestampBinding.frAds);
        AdsManager.INSTANCE.loadBannerHome(this, timestampBinding.frBanner);
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
                        }
                    });
                }
            }
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