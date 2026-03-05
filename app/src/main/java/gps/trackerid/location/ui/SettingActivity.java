package gps.trackerid.location.ui;

import static gps.trackerid.location.utils.Global.mLog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;

import com.facebook.shimmer.ShimmerFrameLayout;

import gps.trackerid.location.R;
import gps.trackerid.location.ads.Preference;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.NativeAdManager;
import gps.trackerid.location.databinding.ActivitySettingBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class SettingActivity extends BaseActivity {
    ActivitySettingBinding binding;
    ShimmerFrameLayout shimmerAds;
    Preference preference;

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
        if (AdsConfig.isShowNative(Global.native_setting, binding.frAds)) {
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
        finish();
    }

    private void extracted() {
        shimmerAds = findViewById(R.id.shimmer_native);

        shimmerAds = findViewById(R.id.shimmer_native);

        boolean isShown = NativeAdManager.getInstance()
                .showNativeAdIfAvailable(
                        this,
                        "native_setting",
                        binding.frAds,
                        shimmerAds
                );

        if (!isShown) {
            mLog("TAG", "Ad not ready, preload again");
            shimmerAds.setVisibility(View.VISIBLE);
            binding.frAds.setVisibility(View.VISIBLE);
//            NativeAdManager.getInstance().preloadNativeAd(SettingActivity.this, getNativeSetting(), R.layout.layout_native_ad_medium, "native_setting");
        }

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
            return;
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
            return;
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