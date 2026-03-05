package gps.trackerid.location.ui;

import static gps.trackerid.location.ads.AdsManagerKt.isNetwork;
import static gps.trackerid.location.utils.Global.dismissInternetDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.module.ads.ERainAd;
import com.ads.module.funtion.AdCallback;
import com.ads.module.ump.IAdConsentCallBack;
import com.ads.module.ump.ITGAdConsent;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.FormError;

import gps.trackerid.location.BuildConfig;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.RemoteUtils;
import gps.trackerid.location.ads.SharedUtils;
import gps.trackerid.location.databinding.ActivitySplashBinding;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.SystmeUtils;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding splashBinding;
    private boolean getConfigSuccess = false;
    private boolean canPersonalized = true;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.post(() -> SystmeUtils.enableFullScreenUi(this, rootView));
        }

        RemoteUtils.INSTANCE.init(() -> getConfigSuccess = true);
        AdsManager.INSTANCE.resetAds();

        if (!SharedUtils.INSTANCE.getValue(SharedUtils.KEY_CONFIRM_CONSENT, false)
                && !SharedUtils.INSTANCE.getValue(SharedUtils.KEY_IS_USER_GLOBAL, false)
                && isNetwork(this)) {
            checkNeedToLoadConsent();
        } else {
            loadingRemoteConfig();
        }
    }

    private void checkNeedToLoadConsent() {
        ITGAdConsent.INSTANCE.loadAndShowConsent(true, new IAdConsentCallBack() {
            @NonNull
            @Override
            public Activity getCurrentActivity() {
                return SplashActivity.this;
            }

            @Override
            public boolean isDebug() {
                return BuildConfig.DEBUG;
            }

            @Override
            public boolean isUnderAgeAd() {
                return false;
            }

            @Override
            public void onConsentError(@NonNull FormError formError) {
                canPersonalized = true;
                loadingRemoteConfig();
            }

            @Override
            public void onConsentStatus(int consentStatus) {
                canPersonalized = consentStatus != ConsentInformation.ConsentStatus.REQUIRED;
            }

            @Override
            public void onConsentSuccess(boolean b) {
                canPersonalized = b;
                handleClickConsent(canPersonalized);
            }

            @Override
            public void onNotUsingAdConsent() {
                SharedUtils.INSTANCE.setValue(SharedUtils.KEY_IS_USER_GLOBAL, true);
                canPersonalized = true;
                loadingRemoteConfig();
            }

            @Override
            public void onRequestShowDialog() {
            }

            @NonNull
            @Override
            public String testDeviceID() {
                return "ED3576D8FCF2F8C52AD8E98B4CFA4005";
            }
        });
    }

    private void handleClickConsent(boolean canPersonalized) {
        if (canPersonalized) {
            SharedUtils.INSTANCE.setValue(SharedUtils.KEY_CONFIRM_CONSENT, true);
        } else {
            ITGAdConsent.INSTANCE.resetConsentDialog();
        }

        loadingRemoteConfig();
    }

    private void loadingRemoteConfig() {
        new CountDownTimer(6500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (getConfigSuccess && millisUntilFinished < 5000) {
                    checkRemoteConfigResult();
                    cancel();
                }
            }

            @Override
            public void onFinish() {
                if (!getConfigSuccess) {
                    checkRemoteConfigResult();
                }
            }

        }.start();
    }

    private void checkRemoteConfigResult() {
        String action = getIntent().getStringExtra(Global.FROM_SHORTCUT);

        if (action != null) {
            switch (action) {
                case Global.ACTION_OPEN_UNINSTALL:
                    AdsManager.INSTANCE.loadBannerSplash(this, splashBinding.mRlBanner);
                    if (RemoteUtils.INSTANCE.getOnInterSplash()) {
                        Log.d("DEV_ITG", "checkRemoteConfigResult: inter_splash_uninstall");
                        ERainAd.getInstance().loadSplashInterstitialAds(this, BuildConfig.inter_splash_uninstall, 25000, 5000, new AdCallback() {
                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                startConfirmUninstallActivity();
                            }
                        });
                    } else {
                        startConfirmUninstallActivity();
                    }
                    break;

                case Global.ACTION_OPEN_NEAR_BY:
                    Log.d("DEV_ITG", "checkRemoteConfigResult: mCallNearByActivity");
                    mCallNearByActivity();
                    break;

                case Global.ACTION_OPEN_TRAFFIC_ALERT:
                    Log.d("DEV_ITG", "checkRemoteConfigResult: mCallTrafficAlertActivity");
                    mCallTrafficAlertActivity();
                    break;

                case Global.ACTION_OPEN_LOCATOR:
                    Log.d("DEV_ITG", "checkRemoteConfigResult: mCallPhoneLocator");
                    mCallPhoneLocator();
                    break;
            }
        } else {
            AdsManager.INSTANCE.loadBannerSplash(this, splashBinding.mRlBanner);
            AdsManager.INSTANCE.loadNativeLanguageNormal(this, SharedUtils.INSTANCE.getValue(SharedUtils.OPEN_APP, false));
            AdsManager.INSTANCE.loadNativeLanguageClick(this, SharedUtils.INSTANCE.getValue(SharedUtils.OPEN_APP, false));
            if (RemoteUtils.INSTANCE.getOnInterSplash()) {
                Log.d("DEV_ITG", "checkRemoteConfigResult: inter_splash action null");
                ERainAd.getInstance().loadSplashInterstitialAds(this, BuildConfig.inter_splash, 25000, 5000, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        mCallNextLanguage();
                    }
                });
            } else {
                mCallNextLanguage();
            }
        }
    }

    private void mCallNextLanguage() {
        startActivity(new Intent(SplashActivity.this, LangActivity.class));
        finish();
    }

    private void mCallNearByActivity() {
        Intent intent = new Intent(SplashActivity.this, NearByActivity.class);
        intent.setAction("android.intent.action.SHORTCUT_NEAR_BY");
        startActivity(intent);
        finish();
    }

    private void mCallTrafficAlertActivity() {
        Intent intent = new Intent(SplashActivity.this, TrafficAlertActivity.class);
        intent.setAction("android.intent.action.SHORTCUT_TRAFFIC_ALERT");
        startActivity(intent);
        finish();
    }

    private void mCallPhoneLocator() {
        Intent intent = new Intent(SplashActivity.this, PhoneLocator.class);
        intent.setAction("android.intent.action.SHORTCUT_PHONE_LOCATOR");
        startActivity(intent);
        finish();
    }

    public final Intent startConfirmUninstallActivity() {
        Intent intent = new Intent(SplashActivity.this, UninstallActivity.class);
        intent.setFlags(268468224);
        intent.putExtra(Global.KEY_TRACKING_SCREEN_FROM, getClass().getSimpleName());
        startActivity(intent);
        return intent;
    }

    @Override
    protected void onDestroy() {
        dismissInternetDialog(SplashActivity.this);
        super.onDestroy();
    }
}