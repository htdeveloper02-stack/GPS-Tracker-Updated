package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage1;
<<<<<<< HEAD
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding11;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding14;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding21;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding24;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen12;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen22;
=======
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage1Click;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage2;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeLanguage2Click;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

<<<<<<< HEAD
=======
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ads.module.ads.ERainAd;
<<<<<<< HEAD
import com.ads.module.util.Preference;
import com.facebook.shimmer.ShimmerFrameLayout;
=======
import com.ads.module.ads.wrapper.ApNativeAd;
import com.ads.module.funtion.AdCallback;
import com.ads.module.util.Preference;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import java.util.ArrayList;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.LanguageAdapter;
import gps.trackerid.location.adshelper.AdsConfig;
<<<<<<< HEAD
import gps.trackerid.location.adshelper.NativeAdManager;
=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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
<<<<<<< HEAD
    //    String adsBeforeid, adsAfterid;
    ShimmerFrameLayout shimmerAds;
    ArrayList<String> mTag = new ArrayList<>();
=======
    String adsBeforeid, adsAfterid;
    ShimmerFrameLayout shimmerAds;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

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
<<<<<<< HEAD
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
=======
            adsBeforeid = getNativeLanguage1();
            if (AdsConfig.isShowNative(Global.native_language_1, languageBinding.frAds) && mType == null) {
                extracted(adsBeforeid);
            } else {
                languageBinding.frAds.setVisibility(GONE);
            }
            adsAfterid = getNativeLanguage1Click();
        } else {
            adsBeforeid = getNativeLanguage2();
            if (AdsConfig.isShowNative(Global.native_language_2, languageBinding.frAds) && mType == null) {
                extracted(adsBeforeid);
            } else {
                languageBinding.frAds.setVisibility(GONE);
            }
            adsAfterid = getNativeLanguage2Click();
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
        }

        if (Global.delay_button_done_language) {
            languageBinding.mLLDone.setVisibility(GONE);
            languageBinding.mTxtDone.setVisibility(GONE);
        } else {
            languageBinding.mLLDone.setVisibility(VISIBLE);
            languageBinding.mTxtDone.setVisibility(VISIBLE);
            languageBinding.mIvDone.setVisibility(GONE);
        }
<<<<<<< HEAD
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
            Log.e("TAG", "Ad not ready, preload again");
            // Optional: preload again if missing
            NativeAdManager.getInstance().preloadNativeAd(LangActivity.this, getNativeLanguage1(), R.layout.layout_native_ad_medium, "native_language_1");
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
=======
    }

    private void extracted(String adsBeforeid) {

        Log.e("TAG", "onAdFailedToLoad:===adsBeforeid=====" + adsBeforeid);
        final ApNativeAd[] mApNativeAd = new ApNativeAd[1];
        ERainAd.getInstance().loadNativeAdResultCallback(this, adsBeforeid, R.layout.layout_native_ad_medium, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                mApNativeAd[0] = nativeAd;
                if (shimmerAds != null) {
                    ERainAd.getInstance().populateNativeAdView(languageActivity, mApNativeAd[0], languageBinding.frAds, shimmerAds);
                }
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                Log.e("TAG", "onAdFailedToLoad:====" + i.getResponseInfo().toString());
                mApNativeAd[0] = null;
//                languageBinding.frAds.removeAllViews();
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                Log.e("TAG", "onAdFailedToLoad:====" + adError.getMessage());
                mApNativeAd[0] = null;
//                languageBinding.frAds.removeAllViews();
            }
        });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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
<<<<<<< HEAD
        clearAd();
    }

    public void clearAd() {
        if (mTag != null && !mTag.isEmpty()) {
            for (int i = 0; i < mTag.size(); i++) {
                NativeAdManager.getInstance().clearAd(mTag.get(i));
            }
        }
        Log.e("TAG", "clearAd:====mTag===" + mTag.size());
    }

    public void setvisibility() {
        if (isFinishing() || isDestroyed()) return;
        if (preference.getBoolean("First")) {
            if (Global.native_language_1_click && mType == null) {
                showNative("native_language_1_click");
            }
        } else {
            if (Global.native_language_2_click && mType == null) {
                showNative("native_language_1_click");
=======
    }

    public void setvisibility() {
        if (preference.getBoolean("First")) {
            if (Global.native_language_1_click && mType == null) {
                extracted(adsAfterid);
            }
        } else {
            if (Global.native_language_2_click && mType == null) {
                extracted(adsAfterid);
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
            }
        }

        if (languageBinding.mLLDone.getVisibility() != VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    languageBinding.mLLDone.setVisibility(VISIBLE);
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
<<<<<<< HEAD

=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
}