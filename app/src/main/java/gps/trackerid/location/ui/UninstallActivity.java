package gps.trackerid.location.ui;

import static gps.trackerid.location.adshelper.AdsConfig.getNativeSurveyUninstall;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;

import com.facebook.shimmer.ShimmerFrameLayout;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.NativeAdManager;
import gps.trackerid.location.databinding.ActivityUninstallBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class UninstallActivity extends BaseActivity {
    ActivityUninstallBinding uninstallBinding;
    UninstallActivity uninstallActivity;
    ShimmerFrameLayout shimmerAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uninstallBinding = ActivityUninstallBinding.inflate(getLayoutInflater());
        setContentView(uninstallBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        uninstallActivity = this;
        uninstallBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        uninstallBinding.mIvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(uninstallActivity, TimestampActivity.class);
                intent.setFlags(268468224);
                intent.putExtra(Global.KEY_TRACKING_SCREEN_FROM, getClass().getSimpleName());
                startActivity(intent);
            }
        });
        uninstallBinding.mIvStillUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(uninstallActivity, SurveyActivity.class);
                intent.setFlags(268468224);
                intent.putExtra(Global.KEY_TRACKING_SCREEN_FROM, getClass().getSimpleName());
                startActivity(intent);
            }
        });
        if (AdsConfig.isShowNative(Global.native_uninstall, uninstallBinding.frAds)) {
            extracted();
        }
        if (Global.native_survey_uninstall) {
            NativeAdManager.getInstance().preloadNativeAd(UninstallActivity.this, getNativeSurveyUninstall(), R.layout.layout_native_ad_medium, "native_survey_uninstall");
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }

    private void onBackCall() {
        Intent intent = new Intent(uninstallActivity, TimestampActivity.class);
        intent.setFlags(268468224);
        intent.putExtra(Global.KEY_TRACKING_SCREEN_FROM, getClass().getSimpleName());
        startActivity(intent);
    }

    private void extracted() {
        shimmerAds = findViewById(R.id.shimmer_native);

        boolean isShown = NativeAdManager.getInstance()
                .showNativeAdIfAvailable(
                        this,
                        "native_uninstall",
                        uninstallBinding.frAds,
                        shimmerAds
                );

        if (!isShown) {
            Log.e("TAG", "Ad not ready, preload again");
            shimmerAds.setVisibility(View.VISIBLE);
            uninstallBinding.frAds.setVisibility(View.VISIBLE);
            // Optional: preload again if missing
//            NativeAdManager.getInstance().preloadNativeAd(LanguageActivity.this, getNativeLanguage1(), R.layout.layout_native_ad_medium, "native_language_1");
        }

    }
}