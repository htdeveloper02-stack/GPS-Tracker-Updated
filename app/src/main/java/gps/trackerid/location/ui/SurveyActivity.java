package gps.trackerid.location.ui;

<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.getNativeSurveyUninstall;

>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
<<<<<<< HEAD
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;

import com.facebook.shimmer.ShimmerFrameLayout;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.NativeAdManager;
=======
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApNativeAd;
import com.ads.module.funtion.AdCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.databinding.ActivitySurveyBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class SurveyActivity extends BaseActivity {
    ActivitySurveyBinding surveyBinding;
    ShimmerFrameLayout shimmerAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyBinding = ActivitySurveyBinding.inflate(getLayoutInflater());
        setContentView(surveyBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        surveyBinding.mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SurveyActivity.this, TimestampActivity.class);
                intent.setFlags(268468224);
                intent.putExtra(Global.KEY_TRACKING_SCREEN_FROM, getClass().getSimpleName());
                startActivity(intent);
            }
        });
        surveyBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();

            }
        });
        surveyBinding.mIvUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOpenScreen();
            }
        });
        if (AdsConfig.isShowNative(Global.native_survey_uninstall, surveyBinding.frAds)) {
            extracted();
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }

    private void onBackCall() {
        Intent intent = new Intent(SurveyActivity.this, TimestampActivity.class);
        intent.setFlags(268468224);
        intent.putExtra(Global.KEY_TRACKING_SCREEN_FROM, getClass().getSimpleName());
        startActivity(intent);
    }

    private void extracted() {
        shimmerAds = findViewById(R.id.shimmer_native);

<<<<<<< HEAD
        boolean isShown = NativeAdManager.getInstance()
                .showNativeAdIfAvailable(
                        this,
                        "native_survey_uninstall",
                        surveyBinding.frAds,
                        shimmerAds
                );

        if (!isShown) {
            Log.e("TAG", "Ad not ready, preload again");
            shimmerAds.setVisibility(View.VISIBLE);
            surveyBinding.frAds.setVisibility(View.VISIBLE);
        }

=======
        final ApNativeAd[] mApNativeAd = new ApNativeAd[1];
        ERainAd.getInstance().loadNativeAdResultCallback(this, getNativeSurveyUninstall(), R.layout.layout_native_ad_medium, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                mApNativeAd[0] = nativeAd;
                ERainAd.getInstance().populateNativeAdView(SurveyActivity.this, mApNativeAd[0], surveyBinding.frAds, shimmerAds);
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                mApNativeAd[0] = null;
                surveyBinding.frAds.removeAllViews();
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                mApNativeAd[0] = null;
                surveyBinding.frAds.removeAllViews();
            }
        });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
    }

    private void mOpenScreen() {
        try {
            String packageName = getPackageName();

            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}