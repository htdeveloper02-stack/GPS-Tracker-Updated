/*
package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding11;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding14;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding21;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding24;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen12;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen22;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ads.module.ads.ERainAd;
import com.ads.module.util.Preference;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.LanguageAdapter;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.InfinityManager;
import gps.trackerid.location.adshelper.NativeUIStart;
import gps.trackerid.location.databinding.ActivityLangBinding;
import gps.trackerid.location.ui.onboard.OnBoardActivity;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.SystmeUtils;

public class LangActivity2 extends AppCompatActivity {
    ActivityLangBinding languageBinding;
    public String mType = null;
    LanguageAdapter languageAdapter;
    public static ArrayList<String> alllang = new ArrayList<>();
    public static String[] lang;
    public static LangActivity2 languageActivity;
    private Map<String, NativeUIStart> currentStates = new HashMap<>();

    public static LangActivity2 getInstance() {
        return languageActivity;
    }

    public String mLang = "en";
    Preference preference;
    //    String adsBeforeid, adsAfterid;
    ShimmerFrameLayout shimmerAds;
    private InfinityManager.AdStateListener listener;

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
       */
/* if (preference.getBoolean("First")) {
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
        }*//*

        listener = states -> {
            runOnUiThread(() -> {
                currentStates = states;

                if (AdsConfig.isShowNative(Global.native_language_2, languageBinding.frAds) && mType == null) {
                    showNAtiveBeforeClick();
                } else {
                    languageBinding.frAds.setVisibility(View.GONE);
                }
            });
        };

        if (Global.delay_button_done_language) {
            languageBinding.mLLDone.setVisibility(GONE);
            languageBinding.mTxtDone.setVisibility(GONE);
        } else {
            languageBinding.mLLDone.setVisibility(VISIBLE);
            languageBinding.mTxtDone.setVisibility(VISIBLE);
            languageBinding.mIvDone.setVisibility(GONE);
        }
        mloadOnBoardingNative();
    }

    private void mloadOnBoardingNative() {
        if (Global.native_onboarding_1_1 || Global.native_onboarding_2_1) {
            String nativeId = preference.getBoolean("First") ? getNativeOnboarding11() : getNativeOnboarding21();
            String TagName = preference.getBoolean("First") ? "native_onboarding_1_1" : "native_onboarding_2_1";
            InfinityManager.mLoadAds(LangActivity2.this, nativeId, R.layout.layout_native_ad_medium, TagName);
        }
        if (Global.native_onboarding_1_4 || Global.native_onboarding_2_4) {
            String nativeId = preference.getBoolean("First") ? getNativeOnboarding14() : getNativeOnboarding24();
            String TagName = preference.getBoolean("First") ? "native_onboarding_1_4" : "native_onboarding_2_4";
            InfinityManager.mLoadAds(LangActivity2.this, nativeId, R.layout.layout_native_ad_medium, TagName);
        }
        if (isShowAds1() || isShowAds2()) {
            String nativeId = preference.getBoolean("First") ? getNativeOnboardingFullscreen12() : getNativeOnboardingFullscreen22();
            String TagName = preference.getBoolean("First") ? "native_onboarding_full_1" : "native_onboarding_full_2";
            InfinityManager.mLoadAds(LangActivity2.this, nativeId, R.layout.layout_native_ad_full, TagName);
        }

    }

    private boolean isShowAds1() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1();
    }

    private boolean isShowAds2() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull2();
    }

    private void showNAtiveBeforeClick() {
        if (!currentStates.isEmpty()) {
            String tag;

            if (!preference.getBoolean("First")) {
                tag = "native_language_2";
            } else {
                tag = "native_language_1";
            }

            NativeUIStart state = currentStates.get(tag);

            if (state instanceof NativeUIStart.Success) {

                NativeUIStart.Success success =
                        (NativeUIStart.Success) state;

                shimmerAds.setVisibility(View.GONE);
                languageBinding.frAds.setVisibility(View.VISIBLE);

                ERainAd.getInstance().populateNativeAdView(
                        LangActivity2.this,
                        success.getNativeAd(),
                        languageBinding.frAds,
                        shimmerAds
                );

            } else if (state instanceof NativeUIStart.Loading) {

                shimmerAds.setVisibility(View.VISIBLE);
                languageBinding.frAds.setVisibility(View.INVISIBLE);

            } else if (state instanceof NativeUIStart.Failed) {

                shimmerAds.setVisibility(View.GONE);
                languageBinding.frAds.setVisibility(View.GONE);
            }
        } else {
            shimmerAds.setVisibility(View.GONE);
            languageBinding.frAds.setVisibility(View.GONE);
        }
    }

    private void showNativeAfterClick() {

        if (!currentStates.isEmpty()) {

            String tag;

            if (preference.getBoolean("First")) {
                tag = "native_language_1_click";
            } else {
                tag = "native_language_2_click";
            }

            NativeUIStart state = currentStates.get(tag);

            if (state instanceof NativeUIStart.Success) {

                NativeUIStart.Success success =
                        (NativeUIStart.Success) state;

                shimmerAds.setVisibility(View.GONE);
                languageBinding.frAds.setVisibility(View.VISIBLE);

                ERainAd.getInstance().populateNativeAdView(
                        LangActivity2.this,
                        success.getNativeAd(),
                        languageBinding.frAds,
                        shimmerAds
                );

            } else if (state instanceof NativeUIStart.Loading) {

                shimmerAds.setVisibility(View.VISIBLE);
                languageBinding.frAds.setVisibility(View.INVISIBLE);

            } else if (state instanceof NativeUIStart.Failed) {

                shimmerAds.setVisibility(View.GONE);
                languageBinding.frAds.setVisibility(View.GONE);
            }
        } else {
            shimmerAds.setVisibility(View.GONE);
            languageBinding.frAds.setVisibility(View.GONE);
        }
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

    public void setvisibility() {
      */
/*  if (preference.getBoolean("First")) {
            if (Global.native_language_1_click && mType == null) {
                extracted();
            }
        } else {
            if (Global.native_language_2_click && mType == null) {
                extracted();
            }
        }*//*

        if (Global.native_language_2_click && mType == null) {
            showNativeAfterClick();
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

    @Override
    protected void onResume() {
        super.onResume();
        InfinityManager.addListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        languageActivity = null;
        InfinityManager.removeListener(listener);
    }

}*/
