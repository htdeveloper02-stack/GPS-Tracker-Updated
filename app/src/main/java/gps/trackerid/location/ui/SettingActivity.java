package gps.trackerid.location.ui;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;

import gps.trackerid.location.R;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.Preference;
import gps.trackerid.location.databinding.ActivitySettingBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class SettingActivity extends BaseActivity {
    ActivitySettingBinding binding;
    Preference preference;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        binding.mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareApp();
            }
        });
        binding.mIvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRateApp();
            }
        });
        binding.mIvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrivacyPolicy(Global.privacy_policy);
            }
        });
        binding.mIvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrivacyPolicy(Global.contact_us);
            }
        });
        binding.mIvAboutUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrivacyPolicy(Global.about_us);
            }
        });
        binding.mLlLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, LangActivity.class);
                intent.putExtra("Type", "Type");
                startActivity(intent);
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });

        AdsManager.INSTANCE.loadNativeSetting(this, binding.frAds);
    }

    private void onBackCall() {
        finish();
    }

    private void mShareApp() {
        try {
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.setType("text/plain");
            intent2.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
            intent2.putExtra("android.intent.extra.TEXT", "\nLet me recommend you this application\n\n" + "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName() + "\n\n");
            startActivity(Intent.createChooser(intent2, "choose one"));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void mRateApp() {
        String packageName = getApplicationContext().getPackageName();
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    private void mPrivacyPolicy(String url) {
        try {
            Global.openChromeCustomTabUrl(SettingActivity.this, url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] lang = getResources().getStringArray(R.array.language);
        preference = new Preference(SettingActivity.this);
        binding.mTxtLanguage.setText(lang[preference.getInteger("LANG", 0)]);
    }
}