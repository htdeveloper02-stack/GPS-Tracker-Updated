package gps.trackerid.location.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;

import com.facebook.shimmer.ShimmerFrameLayout;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.NativeAdManager;
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