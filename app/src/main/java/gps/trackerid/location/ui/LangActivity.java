package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding11;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding14;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding21;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding24;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen12;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen22;
import static gps.trackerid.location.utils.Global.mLog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ads.module.ads.ERainAd;
import com.ads.module.util.Preference;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.LanguageAdapter;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.NativeAdManager;
import gps.trackerid.location.databinding.ActivityLangBinding;
import gps.trackerid.location.ui.onboard.OnBoardActivity;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.SystmeUtils;

public class LangActivity extends AppCompatActivity {
    ActivityLangBinding languageBinding;
    public String mType = null;
    LanguageAdapter languageAdapter;
    public static ArrayList<String> alllang = new ArrayList<>();
    public static String[] lang;
    public static LangActivity languageActivity;

    public static LangActivity getInstance() {
        return languageActivity;
    }

    public String mLang = "en";
    Preference preference;

    //    String adsBeforeid, adsAfterid;
    ShimmerFrameLayout shimmerAds;
    ArrayList<String> mTag = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageBinding = ActivityLangBinding.inflate(getLayoutInflater());
        setContentView(languageBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.post(() -> SystmeUtils.enableFullScreenUi(this, rootView));
        }
        languageActivity = this;
        preference = new Preference(languageActivity);
        if (preference != null) {
            mLang = preference.getSavedLanguage();
        }
        if (getIntent().hasExtra("Type")) {
            mType = getIntent().getStringExtra("Type");
        }
        if (mType != null) {
            languageBinding.mIvBack.setVisibility(VISIBLE);
        }
        shimmerAds = findViewById(R.id.shimmer_native);
        lang = getResources().getStringArray(R.array.language);
        alllang = getAllLang();
        languageBinding.listlang.setLayoutManager(new LinearLayoutManager(this));
        languageAdapter = new LanguageAdapter(languageActivity, alllang);
        languageBinding.listlang.setAdapter(languageAdapter);
        languageBinding.mLLDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.delay_button_done_language) {
                    languageAdapter.setLanguage();
                }
//                    startActivity(new Intent(languageActivity, OnBoardActivity.class));
//                    finish();
//                } else {
                if (mType != null) {
//                    changeAppLanguage(preference.getSavedLanguage());
//                    finish();
                    if (!mLang.equalsIgnoreCase(preference.getSavedLanguage())) {
                        restartApp();
                    } else {
                        finish();
                    }
                } else {
                    startActivity(new Intent(languageActivity, OnBoardActivity.class));
                    finish();
                }
            }
        });
        languageBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (preference.getBoolean("First")) {

            if (AdsConfig.isShowNative(Global.native_language_1, languageBinding.frAds) && mType == null) {
                showNative("native_language_1");
            } else {
                languageBinding.frAds.setVisibility(GONE);
            }
        } else {
            if (AdsConfig.isShowNative(Global.native_language_2, languageBinding.frAds) && mType == null) {
                showNative("native_language_2");
            } else {
                languageBinding.frAds.setVisibility(GONE);
            }
        }

        if (Global.delay_button_done_language) {
            languageBinding.mLLDone.setVisibility(GONE);
            languageBinding.mTxtDone.setVisibility(GONE);
        } else {
            languageBinding.mLLDone.setVisibility(VISIBLE);
            languageBinding.mTxtDone.setVisibility(VISIBLE);
            languageBinding.mIvDone.setVisibility(GONE);
        }

        if (mType == null) {
            mloadOnBoardingNative();
        }
    }

    private void showNative(String adTag) {
        if (isFinishing() || isDestroyed()) return;
        if (!mTag.contains(adTag)) {
            mTag.add(adTag);
        }

        if (languageBinding == null || languageBinding.frAds == null) {
            Log.d("AdDebug", "Ad container is null.");
            return;
        }
        shimmerAds = findViewById(R.id.shimmer_native);

        boolean isShown = NativeAdManager.getInstance().showNativeAdIfAvailable(
                this,
                adTag,
                languageBinding.frAds,
                shimmerAds
        );

        if (!isShown) {
            if (isFinishing() || isDestroyed()) return;
            mLog("TAG", "Ad not ready, preload again");
            // Optional: preload again if missing
//            NativeAdManager.getInstance().preloadNativeAd(LangActivity.this, getNativeLanguage1(), R.layout.layout_native_ad_medium, "native_language_1");
        }
    }

    private void showNativeAfterClick(String adTag) {
        if (isFinishing() || isDestroyed()) return;
        if (!mTag.contains(adTag)) {
            mTag.add(adTag);
        }

        if (languageBinding == null || languageBinding.frAds == null) {
            Log.d("AdDebug", "Ad container is null.");
            return;
        }
        shimmerAds = findViewById(R.id.shimmer_native);

        boolean isShown = NativeAdManager.getInstance().showNativeAdIfAvailable(
                this,
                adTag,
                languageBinding.frAds,
                shimmerAds
        );

        if (!isShown) {
            if (isFinishing() || isDestroyed()) return;
            mLog("TAG", "Ad not ready, preload again");
            // Optional: preload again if missing
//            NativeAdManager.getInstance().preloadNativeAd(LangActivity.this, getNativeLanguage1(), R.layout.layout_native_ad_medium, "native_language_1");
        }
    }

    private void mloadOnBoardingNative() {
        if (Global.native_onboarding_1_1 || Global.native_onboarding_2_1) {
            String nativeId = preference.getBoolean("First") ? getNativeOnboarding11() : getNativeOnboarding21();
            String TagName = preference.getBoolean("First") ? "native_onboarding_1_1" : "native_onboarding_2_1";
            NativeAdManager.getInstance().preloadNativeAd(LangActivity.this, nativeId, R.layout.layout_native_ad_medium, TagName);
        }
        if (Global.native_onboarding_1_4 || Global.native_onboarding_2_4) {
            String nativeId = preference.getBoolean("First") ? getNativeOnboarding14() : getNativeOnboarding24();
            String TagName = preference.getBoolean("First") ? "native_onboarding_1_4" : "native_onboarding_2_4";
            NativeAdManager.getInstance().preloadNativeAd(LangActivity.this, nativeId, R.layout.layout_native_ad_medium, TagName);
        }
        if (isShowAds1() || isShowAds2()) {
            String nativeId = preference.getBoolean("First") ? getNativeOnboardingFullscreen12() : getNativeOnboardingFullscreen22();
            String TagName = preference.getBoolean("First") ? "native_onboarding_full_1" : "native_onboarding_full_2";
            NativeAdManager.getInstance().preloadNativeAd(LangActivity.this, nativeId, R.layout.layout_native_ad_full, TagName);
        }
    }

    private boolean isShowAds1() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1() || Global.native_onboarding_fullscreen_1_2;
    }

    private boolean isShowAds2() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull2() || Global.native_onboarding_fullscreen_2_2;
    }


    public void restartApp() {
        Global.ISLanguageChange = true;

        Intent intent = new Intent(languageActivity, TimestampActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();

    }

    public static ArrayList<String> getAllLang() {
        if (alllang == null || alllang.size() <= 0) {
            for (int i = 0; i < lang.length; i++) {
                alllang.add(lang[i]);
            }
        }
        return alllang;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        languageActivity = null;

        clearAd();
    }

    public void clearAd() {
        if (mTag != null && !mTag.isEmpty()) {
            for (int i = 0; i < mTag.size(); i++) {
                NativeAdManager.getInstance().clearAd(mTag.get(i));
            }
        }
        mLog("TAG", "clearAd:====mTag===" + mTag.size());
    }

    public void setvisibility() {
        if (isFinishing() || isDestroyed()) return;
        if (languageBinding == null) return;
        if (preference.getBoolean("First")) {
            if (Global.native_language_1_click && mType == null) {
                if (!isFinishing() && !isDestroyed()) {
                    showNativeAfterClick("native_language_1_click");
                }
            }
        } else {
            if (Global.native_language_2_click && mType == null) {
                if (!isFinishing() && !isDestroyed()) {
                    showNativeAfterClick("native_language_2_click");
                }
            }
        }

        if (languageBinding.mLLDone.getVisibility() != VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing() && !isDestroyed() && languageBinding != null) {
                        languageBinding.mLLDone.setVisibility(View.VISIBLE);
                    }
                }
            }, 500);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Get saved language or default to English
//        if (getIntent() != null) {
//            if (getIntent().hasExtra("Type")) {
//                mType = getIntent().getStringExtra("Type");
//            }
//            if (mType != null) {
//                preference = new Preference(newBase);
//                String lang = preference.getSavedLanguage();
//                LocaleHelper.setLocale(newBase, lang);
//            }
//        }
        super.attachBaseContext(newBase);
    }
}