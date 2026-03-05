package gps.trackerid.location.ui;

import static gps.trackerid.location.ads.AdsManagerKt.isNetwork;
import static gps.trackerid.location.utils.Global.dismissInternetDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.module.ads.ERainAd;
import com.ads.module.funtion.AdCallback;
import com.ads.module.ump.IAdConsentCallBack;
import com.ads.module.ump.ITGAdConsent;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.FormError;

import java.util.Objects;

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
            public void onConsentError(FormError formError) {
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
        AdsManager.INSTANCE.loadBannerSplash(this, splashBinding.mRlBanner);
        if (!Objects.equals(getIntent().getStringExtra(Global.FROM_SHORTCUT), Global.ACTION_OPEN_UNINSTALL)) {
            ERainAd.getInstance().loadSplashInterstitialAds(this, BuildConfig.inter_splash, 25000, 5000, new AdCallback() {
                @Override
                public void onNextAction() {
                    super.onNextAction();
                    mCallNextLanguage();
                }
            });
        } else {
            ERainAd.getInstance().loadSplashInterstitialAds(this, BuildConfig.inter_splash_uninstall, 25000, 5000, new AdCallback() {
                @Override
                public void onNextAction() {
                    super.onNextAction();
                    startConfirmUninstallActivity();
                }
            });
        }
    }

    private void mCallNextLanguage() {
        startActivity(new Intent(SplashActivity.this, LangActivity.class));
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