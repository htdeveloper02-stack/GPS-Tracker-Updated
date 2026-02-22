package gps.trackerid.location.ui.onboard;

<<<<<<< HEAD
import static gps.trackerid.location.adshelper.AdsConfig.getInterOnboarding;
=======
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding11;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding14;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding21;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboarding24;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen12;
import static gps.trackerid.location.adshelper.AdsConfig.getNativeOnboardingFullscreen22;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
=======
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ads.module.ads.ERainAd;
<<<<<<< HEAD
import com.ads.module.util.Preference;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.adshelper.InterstitialAdManager;
import gps.trackerid.location.adshelper.NativeAdManager;
import gps.trackerid.location.adshelper.NativeUIStart;
=======
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
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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
<<<<<<< HEAD
    //    String adsid, adsId4, fullads;
    public ArrayList<String> mPermissions;
    private Map<String, NativeUIStart> currentStates = new HashMap<>();
=======
    String adsid, adsId4, fullads;
    public ArrayList<String> mPermissions;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onboardBinding = ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(onboardBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onBoardActivity = this;

        preference = new Preference(onBoardActivity);
<<<<<<< HEAD

//        if (preference.getBoolean("First")) {
//            adsid = getNativeOnboarding11();
//            adsId4 = getNativeOnboarding14();
//            fullads = getNativeOnboardingFullscreen12();
//        } else {
//            adsid = getNativeOnboarding21();
//            adsId4 = getNativeOnboarding24();
//            fullads = getNativeOnboardingFullscreen22();
//        }
        setViewPager();

        if (ERainAd.getInstance().getShouldDisplayWidgetUninstall()) {
            initShortCut();
        }
        if (Global.inter_onboarding && ERainAd.getInstance().getShouldDisplayInterOnboarding() && Global.isInternetConnected(onBoardActivity)) {
            InterstitialAdManager.preload(this, getInterOnboarding(), "inter_onboarding");
        }
=======
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
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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
<<<<<<< HEAD
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1() || Global.native_onboarding_fullscreen_1_2;
    }

    private boolean isShowAds2() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull2() || Global.native_onboarding_fullscreen_2_2;
=======
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1();
    }

    private boolean isShowAds2() {
        return ERainAd.getInstance().getShouldDisplayNativeOnboardingFull2();
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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
<<<<<<< HEAD
                shimmerAds = view.findViewById(R.id.shimmer_native);
                if (AdsConfig.isShowNative(preference.getBoolean("First") ? Global.native_onboarding_1_1 : Global.native_onboarding_2_1, frAds)) {
                    String TagName = preference.getBoolean("First") ? "native_onboarding_1_1" : "native_onboarding_2_1";
                    showNative(TagName, frAds, shimmerAds);
=======
                if (AdsConfig.isShowNative(preference.getBoolean("First") ? Global.native_onboarding_1_1 : Global.native_onboarding_2_1, frAds)) {
                    shimmerAds = view.findViewById(R.id.shimmer_native);
                    extracted(adsid, frAds, shimmerAds);
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
                }
            }
            if (isShowAds()) {
                if (position == 2) {
                    frAds = view.findViewById(R.id.fr_ads);
<<<<<<< HEAD
                    shimmerAds = view.findViewById(R.id.shimmer_native);
                    if (AdsConfig.isShowNative(preference.getBoolean("First") ? isShowAds1() : isShowAds2(), frAds)) {
                        String TagName = preference.getBoolean("First") ? "native_onboarding_full_1" : "native_onboarding_full_2";
                        showNative(TagName, frAds, shimmerAds);
=======
                    if (AdsConfig.isShowNative(preference.getBoolean("First") ? isShowAds1() : isShowAds2(), frAds)) {
                        shimmerAds = view.findViewById(R.id.shimmer_native);
                        extractedFull(fullads, frAds, shimmerAds);
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
                    }
                }
            }
            if (position == (layouts.length - 1)) {
                frAds = view.findViewById(R.id.fr_ads);
<<<<<<< HEAD
                shimmerAds = view.findViewById(R.id.shimmer_native);
                if (AdsConfig.isShowNative(preference.getBoolean("First") ? Global.native_onboarding_1_4 : Global.native_onboarding_2_4, frAds)) {
                    String TagName = preference.getBoolean("First") ? "native_onboarding_1_4" : "native_onboarding_2_4";
                    showNative(TagName, frAds, shimmerAds);
=======
                if (AdsConfig.isShowNative(preference.getBoolean("First") ? Global.native_onboarding_1_4 : Global.native_onboarding_2_4, frAds)) {
                    shimmerAds = view.findViewById(R.id.shimmer_native);
                    extracted(adsId4, frAds, shimmerAds);
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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

<<<<<<< HEAD
    private void showNative(String adTag, FrameLayout frAds, ShimmerFrameLayout shimmerAds) {
        boolean isShown = NativeAdManager.getInstance()
                .showNativeAdIfAvailable(
                        this,
                        adTag,
                        frAds,
                        shimmerAds
                );

        if (!isShown) {
            Log.e("TAG", "Ad not ready, preload again");
            shimmerAds.setVisibility(View.VISIBLE);
            frAds.setVisibility(View.VISIBLE);
            // Optional: preload again if missing
//            NativeAdManager.getInstance().preloadNativeAd(OnBoardActivity.this, getNativeLanguage1(), R.layout.layout_native_ad_medium, "native_language_1");
        }
=======
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
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onBoardActivity = null;
<<<<<<< HEAD

=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
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

<<<<<<< HEAD
    private void showNative1(FrameLayout frAds) {
        if (!currentStates.isEmpty()) {
            String tag;

            if (preference.getBoolean("First")) {
                tag = "native_onboarding_1_1";
            } else {
                tag = "native_onboarding_2_1";
            }

            NativeUIStart state = currentStates.get(tag);

            if (state instanceof NativeUIStart.Success) {

                NativeUIStart.Success success =
                        (NativeUIStart.Success) state;

                shimmerAds.setVisibility(View.GONE);
                frAds.setVisibility(View.VISIBLE);

                ERainAd.getInstance().populateNativeAdView(
                        OnBoardActivity.this,
                        success.getNativeAd(),
                        frAds,
                        shimmerAds
                );

            } else if (state instanceof NativeUIStart.Loading) {

                shimmerAds.setVisibility(View.VISIBLE);
                frAds.setVisibility(View.INVISIBLE);

            } else if (state instanceof NativeUIStart.Failed) {

                shimmerAds.setVisibility(View.GONE);
                frAds.setVisibility(View.GONE);
            }
        } else {
            shimmerAds.setVisibility(View.GONE);
            frAds.setVisibility(View.GONE);
        }

    }

    private void showNative4(FrameLayout frAds) {
        if (!currentStates.isEmpty()) {
            String tag;

            if (preference.getBoolean("First")) {
                tag = "native_onboarding_1_4";
            } else {
                tag = "native_onboarding_2_4";
            }

            NativeUIStart state = currentStates.get(tag);

            if (state instanceof NativeUIStart.Success) {

                NativeUIStart.Success success =
                        (NativeUIStart.Success) state;

                shimmerAds.setVisibility(View.GONE);
                frAds.setVisibility(View.VISIBLE);

                ERainAd.getInstance().populateNativeAdView(
                        OnBoardActivity.this,
                        success.getNativeAd(),
                        frAds,
                        shimmerAds
                );

            } else if (state instanceof NativeUIStart.Loading) {

                shimmerAds.setVisibility(View.VISIBLE);
                frAds.setVisibility(View.INVISIBLE);

            } else if (state instanceof NativeUIStart.Failed) {

                shimmerAds.setVisibility(View.GONE);
                frAds.setVisibility(View.GONE);
            }
        } else {
            shimmerAds.setVisibility(View.GONE);
            frAds.setVisibility(View.GONE);
        }
    }

    private void showFullNative(FrameLayout frAds) {
        if (!currentStates.isEmpty()) {

            String tag;

            if (preference.getBoolean("First")) {
                tag = "native_onboarding_full_1";
            } else {
                tag = "native_onboarding_full_2";
            }

            NativeUIStart state = currentStates.get(tag);

            if (state instanceof NativeUIStart.Success) {

                NativeUIStart.Success success =
                        (NativeUIStart.Success) state;

                shimmerAds.setVisibility(View.GONE);
                frAds.setVisibility(View.VISIBLE);

                ERainAd.getInstance().populateNativeAdView(
                        OnBoardActivity.this,
                        success.getNativeAd(),
                        frAds,
                        shimmerAds
                );

            } else if (state instanceof NativeUIStart.Loading) {

                shimmerAds.setVisibility(View.VISIBLE);
                frAds.setVisibility(View.INVISIBLE);

            } else if (state instanceof NativeUIStart.Failed) {

                shimmerAds.setVisibility(View.GONE);
                frAds.setVisibility(View.GONE);
            }
        } else {
            shimmerAds.setVisibility(View.GONE);
            frAds.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setViewPager() {
        if (isShowAds()) {
            layouts = new int[]{R.layout.layout_intro1, R.layout.layout_intro2, R.layout.layout_native, R.layout.layout_intro3, R.layout.layout_intro4};
        } else {
            layouts = new int[]{R.layout.layout_intro1, R.layout.layout_intro2, R.layout.layout_intro3, R.layout.layout_intro4};
        }

        myViewPagerAdapter = new MyViewPagerAdapter();
        onboardBinding.viewPager.setAdapter(myViewPagerAdapter);
        onboardBinding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }
=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

}