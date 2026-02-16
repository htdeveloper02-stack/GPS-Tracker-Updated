package gps.trackerid.location.ui;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;

import com.ads.module.ads.ERainAd;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.databinding.ActivityCompassBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.ui.compassui.NormalFragment;
import gps.trackerid.location.ui.compassui.SateliteFragment;
import gps.trackerid.location.ui.compassui.StandardFragment;
import gps.trackerid.location.utils.Global;

public class CompassActivity extends BaseActivity {
    ActivityCompassBinding compassBinding;
    StandardFragment standardFragment;
    NormalFragment normalFragment;
    SateliteFragment sateliteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compassBinding = ActivityCompassBinding.inflate(getLayoutInflater());
        setContentView(compassBinding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        standardFragment = new StandardFragment();
        normalFragment = new NormalFragment();
        sateliteFragment = new SateliteFragment();
        compassBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        compassBinding.mTxtStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectColor(1);
            }
        });
        compassBinding.mTxtNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectColor(2);
            }
        });
        compassBinding.mTxtSatelite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectColor(3);
            }
        });
        compassBinding.mTxtStandard.performClick();
        if (Global.banner_all && Global.isInternetConnected(CompassActivity.this)) {
            compassBinding.mRlBanner.setVisibility(VISIBLE);
            ERainAd.getInstance().loadBanner(this, getBannerAll());
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }

    private void onBackCall() {
        finish();
    }

    private void mSelectColor(int i) {
        compassBinding.mTxtStandard.setBackground(null);
        compassBinding.mTxtNormal.setBackground(null);
        compassBinding.mTxtSatelite.setBackground(null);
        compassBinding.mTxtStandard.setTextColor(getResources().getColor(R.color.mtxtunselect));
        compassBinding.mTxtNormal.setTextColor(getResources().getColor(R.color.mtxtunselect));
        compassBinding.mTxtSatelite.setTextColor(getResources().getColor(R.color.mtxtunselect));
        switch (i) {
            case 3:
                mSetFragment(3);
                compassBinding.mTxtSatelite.setBackground(ContextCompat.getDrawable(CompassActivity.this, R.drawable.bg_compass));
                compassBinding.mTxtSatelite.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                mSetFragment(2);
                compassBinding.mTxtNormal.setBackground(ContextCompat.getDrawable(CompassActivity.this, R.drawable.bg_compass));
                compassBinding.mTxtNormal.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                mSetFragment(1);
                compassBinding.mTxtStandard.setBackground(ContextCompat.getDrawable(CompassActivity.this, R.drawable.bg_compass));
                compassBinding.mTxtStandard.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void mSetFragment(int z) {
        if (z == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, standardFragment).commit();
        } else if (z == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, normalFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, sateliteFragment).commit();
        }
    }
}