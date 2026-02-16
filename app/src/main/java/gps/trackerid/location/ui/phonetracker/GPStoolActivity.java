package gps.trackerid.location.ui.phonetracker;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
import static gps.trackerid.location.adshelper.AdsConfig.getNativePhoneLocator;
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;
import static gps.trackerid.location.adshelper.AdsConfig.loadHomeInterstitialAds;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;

import com.ads.module.ads.ERainAd;
import com.ads.module.funtion.AdCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.databinding.ActivityGpstoolBinding;
import gps.trackerid.location.ui.CompassActivity;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class GPStoolActivity extends BaseActivity {
    ActivityGpstoolBinding activityGpstoolBinding;
    GPStoolActivity gpStoolActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGpstoolBinding = ActivityGpstoolBinding.inflate(getLayoutInflater());
        setContentView(activityGpstoolBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gpStoolActivity = this;

        activityGpstoolBinding.mIvSpeedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(Speedometer.class);
            }
        });
        activityGpstoolBinding.mIvAreacode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(AreaCodes.class);
            }
        });
        activityGpstoolBinding.mIvStopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(StopwatchAct.class);
            }
        });
        activityGpstoolBinding.mIvCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(CompassActivity.class);
            }
        });
        activityGpstoolBinding.mIvLevelMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextCallActivity(LevelMeterActivity.class);
            }
        });
        activityGpstoolBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        if (Global.banner_all && Global.isInternetConnected(gpStoolActivity)) {
            activityGpstoolBinding.mRlBanner.setVisibility(VISIBLE);
            ERainAd.getInstance().loadBanner(this, getBannerAll());
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }
    private void mNextCallActivity(Class<?> activityClass) {
        startActivity(new Intent(gpStoolActivity, activityClass));
    }
    private void onBackCall() {
        loadBAckInterstitialAds(gpStoolActivity, new AdsConfig.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpStoolActivity = null;
    }
}