package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;
import static gps.trackerid.location.adshelper.AdsConfig.loadHomeInterstitialAds;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;

import com.ads.module.ads.ERainAd;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import gps.trackerid.location.R;
<<<<<<< HEAD
import gps.trackerid.location.adshelper.InterstitialAdManager;
=======
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.database.FirebaseRequestHelper;
import gps.trackerid.location.database.FirebaseUserHelper;
import gps.trackerid.location.databinding.ActivityAdduserBinding;
import gps.trackerid.location.models.users.CurrentUserUtil;
import gps.trackerid.location.models.users.DataUser;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.ExceptionUtil;
import gps.trackerid.location.utils.Global;
import io.github.g00fy2.quickie.QRResult;
import io.github.g00fy2.quickie.ScanCustomCode;
import io.github.g00fy2.quickie.config.ScannerConfig;
import kotlin.collections.CollectionsKt;

public class AddUserActivity extends BaseActivity {
    ActivityAdduserBinding adduserBinding;
    AddUserActivity addUserActivity;
    private ActivityResultLauncher<ScannerConfig> scanCustomCode =
            registerForActivityResult(new ScanCustomCode(), new ActivityResultCallback() {
                @Override
                public final void onActivityResult(Object obj) {
                    scanCustomCodeCall((QRResult) obj);
                }
            });
    FirebaseRequestHelper firebaseRequestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adduserBinding = ActivityAdduserBinding.inflate(getLayoutInflater());
        setContentView(adduserBinding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addUserActivity = this;
        firebaseRequestHelper = FirebaseRequestHelper.INSTANCE;
        adduserBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        adduserBinding.mIvMyQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                InterstitialAdManager.showIfReady(
                        AddUserActivity.this,
                        "inter_home",
                        () -> {
                            startActivity(new Intent(addUserActivity, ShareMyQrActivity.class));
                        }
                );

=======
                loadHomeInterstitialAds(addUserActivity, new AdsConfig.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(addUserActivity, ShareMyQrActivity.class));

                    }
                });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
            }
        });
        adduserBinding.mIvQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askLocationPermission();
            }
        });
        adduserBinding.mIvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!adduserBinding.edtEnteredCode.getText().toString().isEmpty()) {
                    adduserBinding.progressFindUser.setVisibility(VISIBLE);
                    String detectedCode = adduserBinding.edtEnteredCode.getText().toString();
                    FirebaseUserHelper.INSTANCE.getUserIfExists(detectedCode, new FirebaseUserHelper.UserResultCallback() {
                        @Override
                        public void onResult(boolean exists, DataUser user) {
                            manageUserDetect(detectedCode, exists, user);
                        }
                    });

                } else {
                    Toast.makeText(addUserActivity, getResources().getString(R.string.enter_code), 0).show();
                }
            }
        });
        if (Global.banner_all && Global.isInternetConnected(addUserActivity)) {
            adduserBinding.mRlBanner.setVisibility(VISIBLE);
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
        if (getIntent() != null) {
            String string = getIntent().getStringExtra("type");
<<<<<<< HEAD
            if (string == null) {
                InterstitialAdManager.showIfReady(
                        AddUserActivity.this,
                        "inter_back",
                        () -> {
                            finish();
                        }
                );

=======
            if (string==null) {
                loadBAckInterstitialAds(addUserActivity, new AdsConfig.MyCallback() {
                    @Override
                    public void callbackCall() {
                        finish();
                    }
                });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
            } else {
                finish();
            }
        } else {
<<<<<<< HEAD
            InterstitialAdManager.showIfReady(
                    AddUserActivity.this,
                    "inter_back",
                    () -> {
                        finish();
                    }
            );

=======
            loadBAckInterstitialAds(addUserActivity, new AdsConfig.MyCallback() {
                @Override
                public void callbackCall() {
                    finish();
                }
            });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
        }
    }

    public void manageUserDetect(String targetUserCode, boolean userFound, DataUser detectedUser) {
        if (!userFound) {
            // Show "User not found" toast
            Toast.makeText(addUserActivity, getResources().getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();

            // Hide progress bar
            adduserBinding.progressFindUser.setVisibility(GONE);
            return;
        }

        // Get current user code
        DataUser currentUser = CurrentUserUtil.getCurrentUser(addUserActivity);
        String currentUserCode = (currentUser != null && currentUser.getCode() != null)
                ? currentUser.getCode()
                : "";

        // Check if the target user is already a friend or requested
        FirebaseRequestHelper firebaseHelper = FirebaseRequestHelper.INSTANCE;
        firebaseHelper.checkFriendOrRequested(currentUserCode, targetUserCode, new FirebaseRequestHelper.CheckFriendCallback() {
            @Override
            public void onResult(boolean areFriends, boolean requestPending) {
                handleUserDetection(detectedUser, targetUserCode, areFriends, requestPending);
            }
        });
    }

    public void handleUserDetection(DataUser dataUser, String userCode, boolean alreadyFriend, boolean requestSent) {

        // Hide progress bar
        adduserBinding.progressFindUser.setVisibility(GONE);

        if (alreadyFriend) {
            Toast.makeText(addUserActivity, getResources().getString(R.string.already_add), Toast.LENGTH_SHORT).show();
        } else if (requestSent) {
            Toast.makeText(addUserActivity, getResources().getString(R.string.already_request), Toast.LENGTH_SHORT).show();
        } else {
            // Show detected user layout
            adduserBinding.layoutDetectedUser.setVisibility(VISIBLE);

            // Set user name and code
            String name = dataUser != null && dataUser.getName() != null ? dataUser.getName() : "Unknown";
            String code = dataUser != null && dataUser.getCode() != null ? dataUser.getCode() : "Unknown";

            adduserBinding.tvDetectedUserName.setText(name);
            adduserBinding.tvDetectedUserCode.setText(code);

            // Set click listener to send request
            adduserBinding.tvSendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendRequestToUser(userCode);
                }
            });
        }
    }

    public void sendRequestToUser(String targetUserCode) {

        // Get current user's code
        DataUser currentUser = CurrentUserUtil.getCurrentUser(addUserActivity);
        String currentUserCode = currentUser != null && currentUser.getCode() != null
                ? currentUser.getCode()
                : "";

        // Send friend request
        firebaseRequestHelper.sendRequest(currentUserCode, targetUserCode);

        // Show confirmation Toast
        Toast.makeText(addUserActivity, getResources().getString(R.string.frd_request), Toast.LENGTH_SHORT).show();

        // Hide the detected user layout
        adduserBinding.layoutDetectedUser.setVisibility(GONE);
    }

    public void askLocationPermission() {
        Dexter.withContext(addUserActivity).withPermissions(CollectionsKt.listOf("android.permission.CAMERA")).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    ScannerConfig.Builder config1 = new ScannerConfig.Builder();
                    config1.setOverlayDrawableRes(Integer.valueOf(R.mipmap.ic_launcher));
                    ScannerConfig config = new ScannerConfig.Builder().build();
                    scanCustomCode.launch(config);
                } else if (report.isAnyPermissionPermanentlyDenied()) {
                    showSettingsDialog();
                } else {

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                if (token != null) {
                    token.continuePermissionRequest();
                }
            }
        }).check();
    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(addUserActivity, R.style.AppAlertDialogStyle);

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

    private void scanCustomCodeCall(QRResult result) {
        if (result instanceof QRResult.QRSuccess) {
            ExceptionUtil.safe(
                    null, // or error callback if needed
                    () -> handleQrSuccess((QRResult.QRSuccess) result)
            );
        }
    }

    private void handleQrSuccess(QRResult.QRSuccess qRResult) {
        String rawValue = qRResult.getContent().getRawValue();
        if (rawValue == null) {
            rawValue = "";
        }
        if (!rawValue.isEmpty()) {
            manageUserDetect(rawValue);
        }
    }

    public void manageUserDetect(String detectedCode) {

        // Show the progress bar
        adduserBinding.progressFindUser.setVisibility(VISIBLE);

        // Fetch the user from Firebase
        FirebaseUserHelper.getUserIfExists(detectedCode, new FirebaseUserHelper.UserResultCallback() {
            @Override
            public void onResult(boolean exists, DataUser user) {
                handleDetectedUser(detectedCode, exists, user);
            }
        });
    }

    public void handleDetectedUser(String detectedCode, boolean userExists, final DataUser dataUser) {

        adduserBinding.progressFindUser.setVisibility(GONE);


        if (!userExists) {
            Toast.makeText(addUserActivity, getResources().getString(R.string.user_not_found), Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current user code
        DataUser currentUser = CurrentUserUtil.getCurrentUser(addUserActivity);
        String currentUserCode = (currentUser != null && currentUser.getCode() != null)
                ? currentUser.getCode() : "";

        // Check if already friends or request sent
        firebaseRequestHelper.checkFriendOrRequested(currentUserCode, detectedCode, new FirebaseRequestHelper.CheckFriendCallback() {
            @Override
            public void onResult(boolean alreadyFriend, boolean requestPending) {
                updateDetectedUserUI(dataUser, detectedCode, alreadyFriend, requestPending);
            }
        });
    }

    /**
     * Updates the UI based on friendship/request status.
     */
    private void updateDetectedUserUI(DataUser dataUser, String detectedCode, boolean alreadyFriend, boolean requestPending) {

        if (alreadyFriend) {
            Toast.makeText(addUserActivity, getResources().getString(R.string.already_add), Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestPending) {
            Toast.makeText(addUserActivity, getResources().getString(R.string.already_request), Toast.LENGTH_SHORT).show();
            return;
        }

        // Show detected user layout
        adduserBinding.layoutDetectedUser.setVisibility(VISIBLE);

        // Set user info
        String name = dataUser != null && dataUser.getName() != null ? dataUser.getName() : "Unknown";
        String code = dataUser != null && dataUser.getCode() != null ? dataUser.getCode() : "Unknown";

        adduserBinding.tvDetectedUserName.setText(name);
        adduserBinding.tvDetectedUserCode.setText(code);

        // Set click listener to send friend request
        adduserBinding.tvSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFriendRequest(detectedCode);
            }
        });
    }

    /**
     * Handles sending a friend request.
     */
    private void sendFriendRequest(String targetUserCode) {

        DataUser currentUser = CurrentUserUtil.getCurrentUser(addUserActivity);
        String currentUserCode = (currentUser != null && currentUser.getCode() != null)
                ? currentUser.getCode() : "";

        firebaseRequestHelper.sendRequest(currentUserCode, targetUserCode);

        Toast.makeText(addUserActivity, getResources().getString(R.string.frd_request), Toast.LENGTH_SHORT).show();

        // Hide detected user layout after sending request
        adduserBinding.layoutDetectedUser.setVisibility(GONE);
    }

}