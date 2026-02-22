package gps.trackerid.location.ui;

<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.getInterOnboarding;
import static gps.trackerid.location.adshelper.AdsConfig.loadInterstitialAds;

>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
<<<<<<< HEAD
import android.os.Handler;
=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.R;
<<<<<<< HEAD
import gps.trackerid.location.adshelper.InterstitialAdManager;
=======
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.databinding.ActivityPermissionBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;

public class PermissionActivity extends BaseActivity {
    ActivityPermissionBinding permissionBinding;
    public ArrayList<String> mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionBinding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(permissionBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (IsCheckPermission()) {
            mCallNext();
        }
        permissionBinding.mIvGrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckPermission();
            }
        });
    }

    private boolean IsTaken(List<String> list, String str) {
        if (ContextCompat.checkSelfPermission(this, str) != 0) {
            list.add(str);
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, str);
        }
        return true;
    }

    private void mCheckPermission() {
        mPermissions = new ArrayList();
        if (!IsTaken(mPermissions, "android.permission.ACCESS_COARSE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.ACCESS_FINE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.CAMERA")) {
            mPermissions.add("android.permission.CAMERA");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!IsTaken(mPermissions, "android.permission.POST_NOTIFICATIONS")) {
                mPermissions.add("android.permission.POST_NOTIFICATIONS");
            }
        }

        if (!mPermissions.isEmpty()) {
            Dexter.withContext(PermissionActivity.this)
                    .withPermissions(mPermissions).withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
<<<<<<< HEAD
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mCallNext();
                                    }
                                },1000);
=======
                                mCallNext();
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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
            mCallNext();
        }
    }

    private boolean IsCheckPermission() {
        mPermissions = new ArrayList();
        if (!IsTaken(mPermissions, "android.permission.ACCESS_COARSE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.ACCESS_FINE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.CAMERA")) {
            mPermissions.add("android.permission.CAMERA");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!IsTaken(mPermissions, "android.permission.POST_NOTIFICATIONS")) {
                mPermissions.add("android.permission.POST_NOTIFICATIONS");
            }
        }

        if (!mPermissions.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void mCallNext() {
<<<<<<< HEAD
        InterstitialAdManager.showIfReady(
                PermissionActivity.this,
                "inter_onboarding",
                () -> {
                    runOnUiThread(() -> {

                        if (isFinishing() || isDestroyed()) return;

                        startActivity(new Intent(PermissionActivity.this, TimestampActivity.class));
                        finish();
                    });
                }
        );

=======
        loadInterstitialAds(PermissionActivity.this, getInterOnboarding(), new AdsConfig.MyCallback() {
            @Override
            public void callbackCall() {
                startActivity(new Intent(PermissionActivity.this, TimestampActivity.class));
                finish();
            }
        });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionActivity.this, R.style.AppAlertDialogStyle);

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

    @Override
    protected void onResume() {
        super.onResume();
//        mCheckPermission();
    }
}
