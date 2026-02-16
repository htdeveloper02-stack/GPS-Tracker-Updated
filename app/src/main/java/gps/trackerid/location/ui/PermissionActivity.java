package gps.trackerid.location.ui;

import static gps.trackerid.location.adshelper.AdsConfig.getInterOnboarding;
import static gps.trackerid.location.adshelper.AdsConfig.loadInterstitialAds;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import gps.trackerid.location.adshelper.AdsConfig;
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
                                mCallNext();
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
        loadInterstitialAds(PermissionActivity.this, getInterOnboarding(), new AdsConfig.MyCallback() {
            @Override
            public void callbackCall() {
                startActivity(new Intent(PermissionActivity.this, TimestampActivity.class));
                finish();
            }
        });
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
