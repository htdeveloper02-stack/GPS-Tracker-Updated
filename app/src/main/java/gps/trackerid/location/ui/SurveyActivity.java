package gps.trackerid.location.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.activity.OnBackPressedCallback;

import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.databinding.ActivitySurveyBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class SurveyActivity extends BaseActivity {
    ActivitySurveyBinding surveyBinding;

    @SuppressLint("SourceLockedOrientationActivity")
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
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });

        AdsManager.INSTANCE.loadNativeSurveyUninstall(this, surveyBinding.frAds);
    }

    private void onBackCall() {
        Intent intent = new Intent(SurveyActivity.this, TimestampActivity.class);
        intent.setFlags(268468224);
        intent.putExtra(Global.KEY_TRACKING_SCREEN_FROM, getClass().getSimpleName());
        startActivity(intent);
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