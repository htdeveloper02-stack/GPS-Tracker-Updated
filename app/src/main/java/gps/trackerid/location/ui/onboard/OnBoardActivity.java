package gps.trackerid.location.ui.onboard;

import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding11;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding14;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding21;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding24;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen12;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen22;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApNativeAd;
import com.ads.module.funtion.AdCallback;
import com.ads.module.util.Preference;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.databinding.ActivityOnboardBinding;
import gps.trackerid.location.ui.NearByActivity;
import gps.trackerid.location.ui.PermissionActivity;
import gps.trackerid.location.ui.PhoneLocator;
import gps.trackerid.location.ui.SplashActivity;
import gps.trackerid.location.ui.TrafficAlertActivity;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class OnBoardActivity extends BaseActivity {
    ActivityOnboardBinding onboardBinding;
    public static OnBoardActivity onBoardActivity;
    private Preference preference;

    public static OnBoardActivity getInstance() {
        return onBoardActivity;
    }

    private MyViewPagerAdapter myViewPagerAdapter;
    private int mPos = 0;
    private int[] layouts;
    String adsid, adsId4, fullads;
    public ArrayList<String> mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onboardBinding = ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(onboardBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onBoardActivity = this;

        preference = new Preference(onBoardActivity);
        if (isShowAds()) {
            layouts = new int[]{R.layout.layout_intro1, R.layout.layout_intro2, R.layout.layout_native, R.layout.layout_intro3, R.layout.layout_intro4};
        } else {
            layouts = new int[]{R.layout.layout_intro1, R.layout.layout_intro2, R.layout.layout_intro3, R.layout.layout_intro4};
        }

        myViewPagerAdapter = new MyViewPagerAdapter();
        onboardBinding.viewPager.setAdapter(myViewPagerAdapter);
        onboardBinding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        if (preference.getBoolean("First")) {
            adsid = getNativeOnboarding11();
            adsId4 = getNativeOnboarding14();
            fullads = getNativeOnboardingFullscreen12();
        } else {
            adsid = getNativeOnboarding21();
            adsId4 = getNativeOnboarding24();
            fullads = getNativeOnboardingFullscreen22();
        }
        if (ERainAd.getInstance().getShouldDisplayWidgetUninstall()) {
            initShortCut();
        }
    }

    private void launchHomeScreen() {
        startActivity(new Intent(onBoardActivity, PermissionActivity.class));
        finish();
    }

    private boolean IsTaken(List<String> list, String str) {
        if (ContextCompat.checkSelfPermission(this, str) != 0) {
            list.add(str);
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, str);
        }
        return true;
    }

    private boolean IsCheckPermission() {
        mPermissions = new ArrayList();
        if (!IsTaken(mPermissions, "android.permission.ACCESS_COARSE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.ACCESS_FINE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.CAMERA")) {
            mPermissions.add("android.permission.CAMERA");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!IsTaken(mPermissions, "android.permission.POST_NOTIFICATIONS")) {
                mPermissions.add("android.permission.POST_NOTIFICATIONS");
            }
        }

        if (!mPermissions.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isShowAds() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1() || ERainAd.getInstance().getShouldDisplayNativeOnboardingFull2();
    }

    private boolean isShowAdsLayout(int pos) {
        return layouts[pos] == R.layout.layout_native;
    }

    private boolean isShowAds1() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1();
    }

    private boolean isShowAds2() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull2();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mPos = position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
    private FrameLayout frAds;
    private ShimmerFrameLayout shimmerAds;

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            if (!isShowAdsLayout(position)) {

                TextView mIvNext = view.findViewById(R.id.mIvNext);
                mIvNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPos == layouts.length - 1) {
                            launchHomeScreen();
                        } else {
                            onboardBinding.viewPager.setCurrentItem(mPos + 1);
                        }
                    }
                });
            } else if (isShowAdsLayout(position)) {
                ImageView mIvNext = view.findViewById(R.id.mIvNext);
                mIvNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mPos == layouts.length - 1) {
                            launchHomeScreen();
                        } else {
                            onboardBinding.viewPager.setCurrentItem(mPos + 1);
                        }
                    }
                });
            }
            if (position == 0) {
                frAds = view.findViewById(R.id.fr_ads);
                if (AdsConfig.isShowNative(preference.getBoolean("First") ? Global.native_onboarding_1_1 : Global.native_onboarding_2_1, frAds)) {
                    shimmerAds = view.findViewById(R.id.shimmer_native);
                    extracted(adsid, frAds, shimmerAds);
                }
            }
            if (isShowAds()) {
                if (position == 2) {
                    frAds = view.findViewById(R.id.fr_ads);
                    if (AdsConfig.isShowNative(preference.getBoolean("First") ? isShowAds1() : isShowAds2(), frAds)) {
                        shimmerAds = view.findViewById(R.id.shimmer_native);
                        extractedFull(fullads, frAds, shimmerAds);
                    }
                }
            }
            if (position == (layouts.length - 1)) {
                frAds = view.findViewById(R.id.fr_ads);
                if (AdsConfig.isShowNative(preference.getBoolean("First") ? Global.native_onboarding_1_4 : Global.native_onboarding_2_4, frAds)) {
                    shimmerAds = view.findViewById(R.id.shimmer_native);
                    extracted(adsId4, frAds, shimmerAds);
                }
            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void extracted(String adsBeforeid, FrameLayout frAds, ShimmerFrameLayout shimmerAds) {
        final ApNativeAd[] mApNativeAd = new ApNativeAd[1];
        ERainAd.getInstance().loadNativeAdResultCallback(this, adsBeforeid, R.layout.layout_native_ad_medium, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                mApNativeAd[0] = nativeAd;
                ERainAd.getInstance().populateNativeAdView(onBoardActivity, mApNativeAd[0], frAds, shimmerAds);
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                mApNativeAd[0] = null;
//                frAds.removeAllViews();
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                mApNativeAd[0] = null;
//                frAds.removeAllViews();
            }
        });
    }

    private void extractedFull(String adsBeforeid, FrameLayout frAds, ShimmerFrameLayout shimmerAds) {

        final ApNativeAd[] mApNativeAd = new ApNativeAd[1];
        ERainAd.getInstance().loadNativeAdResultCallback(this, adsBeforeid, R.layout.layout_native_ad_full, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                mApNativeAd[0] = nativeAd;
                ERainAd.getInstance().populateNativeAdView(onBoardActivity, mApNativeAd[0], frAds, shimmerAds);
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                mApNativeAd[0] = null;
//                frAds.removeAllViews();
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                mApNativeAd[0] = null;
//                frAds.removeAllViews();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onBoardActivity = null;
    }

    private void initShortCut() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return;

        try {
            ShortcutManager shortcutManager =
                    (ShortcutManager) getSystemService(ShortcutManager.class);

            if (shortcutManager == null) return;

            shortcutManager.removeAllDynamicShortcuts();

            ArrayList<ShortcutInfo> shortcuts = new ArrayList<>();


            shortcuts.add(
                    new ShortcutInfo.Builder(this, "shortcut_locator")
                            .setShortLabel(getString(R.string.phonelocator))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_locator))
                            .setIntent(createShortcutIntent(
                                    PhoneLocator.class,
                                    "android.intent.action.SHORTCUT_PHONE_LOCATOR",
                                    Global.ACTION_OPEN_LOCATOR))
                            .setRank(0)
                            .build()
            );
            shortcuts.add(
                    new ShortcutInfo.Builder(this, "hortcut_traffic")
                            .setShortLabel(getString(R.string.traffic))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_traffic))
                            .setIntent(createShortcutIntent(
                                    TrafficAlertActivity.class,
                                    "android.intent.action.SHORTCUT_TRAFFIC_ALERT",
                                    Global.ACTION_OPEN_HOME))
                            .setRank(1)
                            .build()
            );
            shortcuts.add(
                    new ShortcutInfo.Builder(this, "shortcut_nearby")
                            .setShortLabel(getString(R.string.nearby))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_nearby))
                            .setIntent(createShortcutIntent(
                                    NearByActivity.class,
                                    "android.intent.action.SHORTCUT_NEAR_BY",
                                    Global.ACTION_OPEN_HOME))
                            .setRank(2)
                            .build()
            );

            shortcuts.add(
                    new ShortcutInfo.Builder(this, "shortcut_uninstall")
                            .setShortLabel(getString(R.string.txt_uninstall))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_uninstall))
                            .setIntent(createShortcutIntent(
                                    SplashActivity.class,
                                    "android.intent.action.SHORTCUT_UNINSTALL_APP",
                                    Global.ACTION_OPEN_UNINSTALL))
                            .setRank(3)
                            .build()
            );

            shortcutManager.setDynamicShortcuts(shortcuts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent createShortcutIntent(
            Class<?> target,
            String action,
            String shortcutType) {

        Intent intent = new Intent(this, target);
        intent.setAction(action);
        intent.putExtra(Global.FROM_SHORTCUT, shortcutType);

        // Clears task properly
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return intent;
    }


}