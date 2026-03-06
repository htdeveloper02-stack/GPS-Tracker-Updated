package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.ads.AdsManagerKt.isNetwork;
import static gps.trackerid.location.ads.PopulateNativeAdViewKt.populateNativeAdView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ads.module.ads.wrapper.ApNativeAd;

import java.util.ArrayList;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.LanguageAdapter;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.PreLoadNativeListener;
import gps.trackerid.location.ads.Preference;
import gps.trackerid.location.ads.RemoteUtils;
import gps.trackerid.location.ads.SharedUtils;
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
    ArrayList<String> mTag = new ArrayList<>();
    private boolean populateAds = false;

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
        lang = getResources().getStringArray(R.array.language);
        alllang = getAllLang();
        languageBinding.listlang.setLayoutManager(new LinearLayoutManager(this));
        languageAdapter = new LanguageAdapter(languageActivity, alllang);
        languageBinding.listlang.setAdapter(languageAdapter);
        languageBinding.mLLDone.setOnClickListener(view -> {
            if (!RemoteUtils.INSTANCE.getDelayButtonDoneLanguage()) {
                languageAdapter.setLanguage();
            }
            if (mType != null) {
                if (!mLang.equalsIgnoreCase(preference.getSavedLanguage())) {
                    restartApp();
                } else {
                    finish();
                }
            } else {
                startActivity(new Intent(languageActivity, OnBoardActivity.class));
                finish();
            }
        });
        languageBinding.mIvBack.setOnClickListener(view -> finish());

        if (RemoteUtils.INSTANCE.getDelayButtonDoneLanguage()) {
            languageBinding.mLLDone.setVisibility(GONE);
            languageBinding.mTxtDone.setVisibility(GONE);
        } else {
            languageBinding.mLLDone.setVisibility(VISIBLE);
            languageBinding.mTxtDone.setVisibility(VISIBLE);
            languageBinding.mIvDone.setVisibility(GONE);
        }

        initAdmob(AdsManager.NativeLanguageType.NORMAL);
        Log.d("DEV_ITG", "Show: getNativeAdLanguageNormal");
    }

    private AdsManager.NativeLanguageType currentNativeType = null;

    private void initAdmob(AdsManager.NativeLanguageType type) {
        currentNativeType = type;
        populateAds = false;
        showAds();

        AdsManager.INSTANCE.setPreLoadNativeCallback(new PreLoadNativeListener() {
            @Override
            public void onLoadNativeSuccess() {

            }

            @Override
            public void onLoadNativeFail() {
                languageBinding.shimmerAds.shimmerNative.setVisibility(GONE);
            }
        });
    }

    private void showAds() {
        if (!isNetwork(this)) {
            languageBinding.shimmerAds.shimmerNative.setVisibility(GONE);
            return;
        }

        if (populateAds) return;

        ApNativeAd nativeAd;
        if (currentNativeType == AdsManager.NativeLanguageType.NORMAL) {
            nativeAd = AdsManager.INSTANCE.getNativeAdLanguageNormal();
        } else {
            nativeAd = AdsManager.INSTANCE.getNativeAdLanguageClick();
        }

        if (nativeAd != null) {
            languageBinding.shimmerAds.shimmerNative.setVisibility(VISIBLE);
            populateNativeAdView(
                    this, nativeAd, languageBinding.frAds, languageBinding.shimmerAds.shimmerNative,
                    (int) RemoteUtils.INSTANCE.getCTAButtonHeight()
            );
            populateAds = true;
        } else {
            languageBinding.shimmerAds.shimmerNative.setVisibility(GONE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        languageActivity = null;
    }

    private boolean isNativeClick = false;

    public void setVisibility() {
        if (isFinishing() || isDestroyed()) return;
        if (languageBinding == null) return;
        if (!isFinishing() && !isDestroyed()) {
            if (!isNativeClick && AdsManager.INSTANCE.getNativeAdLanguageClick() != null) {
                Log.d("DEV_ITG", "Show: getNativeAdLanguageClick");
                initAdmob(AdsManager.NativeLanguageType.CLICK);
                isNativeClick = true;
            }
        }
        if (languageBinding.mLLDone.getVisibility() != VISIBLE) {
            new Handler().postDelayed(() -> {
                if (!isFinishing() && !isDestroyed() && languageBinding != null) {
                    languageBinding.mLLDone.setVisibility(View.VISIBLE);
                }
            }, 500);
        }
    }
}