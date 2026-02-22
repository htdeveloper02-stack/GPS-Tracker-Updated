package gps.trackerid.location.ui;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerSplash;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerSplashUninstall;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage1;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage1Click;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage2;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage2Click;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeUninstall;
import static gps.trackerid.location.adshelper.AdsConfig.loadSplashInterstitialAds;
import static gps.trackerid.location.adshelper.AdsConfig.loadUnsintallInterstitialAds;
import static gps.trackerid.location.utils.Global.showInternetDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.module.admob.Admob;
import com.ads.module.admob.AppOpenManager;
import com.ads.module.ads.ERainAd;
import com.ads.module.ump.IAdConsentCallBack;
import com.ads.module.ump.ITGAdConsent;
import com.ads.module.util.Preference;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.FormError;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import gps.trackerid.location.BuildConfig;
import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.NativeAdManager;
import gps.trackerid.location.databinding.ActivitySplashBinding;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.SystmeUtils;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding splashBinding;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean isFromShortcut;
    Preference preference;
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
    }

    public void mCallRemoteConfigData() {
        if (Global.isInternetConnected(this)) {

            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1).build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            try {
                                Global.iSGetAds = true;
                                AdsConfig.mSetRemoteData(mFirebaseRemoteConfig);
                                if (Global.native_language_1 || Global.native_language_2) {
                                    String nativeId = preference.getBoolean("First") ? getNativeLanguage1() : getNativeLanguage2();
                                    String TagName = preference.getBoolean("First") ? "native_language_1" : "native_language_2";
                                    NativeAdManager.getInstance().preloadNativeAd(SplashActivity.this, nativeId, R.layout.layout_native_ad_medium, TagName);
                                }
                                if (Global.native_language_1_click || Global.native_language_2_click) {
                                    String nativeId = preference.getBoolean("First") ? getNativeLanguage1Click() : getNativeLanguage2Click();
                                    String TagName = preference.getBoolean("First") ? "native_language_1_click" : "native_language_2_click";
                                    NativeAdManager.getInstance().preloadNativeAd(SplashActivity.this, nativeId, R.layout.layout_native_ad_medium, TagName);
                                }
                                if (Global.open_resume) {
                                    AppOpenManager.getInstance().enableAppResume();
                                } else {
                                    AppOpenManager.getInstance().disableAppResume();
                                }
                                mCallNext();
                            } catch (Exception e) {
                                e.printStackTrace();
                                mCallNext();
                            }

                        } else {
                            Log.e("RemoteConfig", "Fetch failed");
                            mCallNext();
                        }
                    });
        } else {
            showInternetDialog(SplashActivity.this);
        }

    }

    private void mCallNext() {
        preference.setLong("height_button_cta", Global.height_button_cta);
        if (isFromShortcut) {
            if (Global.banner_splash_uninstall && Global.isInternetConnected(SplashActivity.this)) {
                splashBinding.mRlBanner.setVisibility(VISIBLE);
                ERainAd.getInstance().loadBanner(SplashActivity.this, getBannerSplashUninstall());
            }
            if (Global.native_uninstall) {
                NativeAdManager.getInstance().preloadNativeAd(SplashActivity.this, getNativeUninstall(), R.layout.layout_native_ad_medium, "native_uninstall");
            }
        } else {
            if (Global.banner_splash && Global.isInternetConnected(SplashActivity.this)) {
                splashBinding.mRlBanner.setVisibility(VISIBLE);
                ERainAd.getInstance().loadBanner(SplashActivity.this, getBannerSplash());
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFromShortcut) {
                    loadUnsintallInterstitialAds(SplashActivity.this, new AdsConfig.MyCallback() {
                        @Override
                        public void callbackCall() {
                            startConfirmUninstallActivity();
                            finish();
                        }
                    });
                } else {
                    loadSplashInterstitialAds(SplashActivity.this, new AdsConfig.MyCallback() {
                        @Override
                        public void callbackCall() {
                            mCallNextLanguage();
                        }
                    });
                }
            }
        }, 1000);

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
                mCallRemoteConfigData();
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
                preference.setBoolean("KEY_IS_USER_GLOBAL", true);
                canPersonalized = true;
                mCallRemoteConfigData();
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
            preference.setBoolean("KEY_CONFIRM_CONSENT", true);
        } else {
            ITGAdConsent.INSTANCE.resetConsentDialog();
        }
        mCallRemoteConfigData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.dismissInternetDialog(SplashActivity.this);
        mCallData();
    }

    private void mCallData() {
        preference = new Preference(SplashActivity.this);
        if (!preference.getBoolean("KEY_IS_USER_GLOBAL", false) && !preference.getBoolean("KEY_CONFIRM_CONSENT", false)) {
            checkNeedToLoadConsent();
        } else {
            mCallRemoteConfigData();
        }
        Intent intent = getIntent();
        this.isFromShortcut = intent != null
                && Global.ACTION_OPEN_UNINSTALL.equals(
                intent.getStringExtra(Global.FROM_SHORTCUT)
        );
    }

    @Override
    protected void onDestroy() {
        Admob.getInstance().dismissDialog();
        super.onDestroy();
    }
}