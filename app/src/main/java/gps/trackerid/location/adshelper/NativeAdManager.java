package gps.trackerid.location.adshelper;

import static gps.trackerid.location.utils.Global.mLog;

import android.app.Activity;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApNativeAd;
import com.ads.module.funtion.AdCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class NativeAdManager {

    private static NativeAdManager instance;
    private final Map<String, ApNativeAd> nativeAdMap = new HashMap<>();

    private NativeAdManager() {
    }

    public static NativeAdManager getInstance() {
        if (instance == null) {
            instance = new NativeAdManager();
        }
        return instance;
    }

    // Preload Ad
    public void preloadNativeAd(Activity activity, String adid, int layout, String adtag) {
        mLog("TAG", "AdManager:====loading for tag==" + adtag + "==if===" + adid);
        ERainAd.getInstance().loadNativeAdResultCallback(activity, adid, layout, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                nativeAdMap.put(adtag, nativeAd);
                mLog("AdManager=====onNativeAdLoaded===", adtag);
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                nativeAdMap.remove(adtag);
                mLog("AdManager=====", "onAdFailedToLoad:=== " + i.getMessage());
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                mLog("AdManager=====", "onAdFailedToShow:=== " + adError.getMessage());

                nativeAdMap.remove(adtag);

            }
        });
    }

    // Show Ad if available
    public boolean showNativeAdIfAvailable(Activity activity, String adTag, FrameLayout adContainer, ShimmerFrameLayout shimmerView) {

        if (activity == null) return false;
        if (activity.isFinishing() || activity.isDestroyed()) return false;
        if (adContainer == null) return false;

        ApNativeAd nativeAd = nativeAdMap.get(adTag);
        if (nativeAd == null) {
            mLog("AdManager====", "Ad Not Available: " + adTag);
            return false;
        }
        // 🔥 Use WeakReference to prevent crash
        WeakReference<Activity> activityRef = new WeakReference<>(activity);

        activity.runOnUiThread(() -> {

            Activity act = activityRef.get();

            if (act == null || act.isFinishing() || act.isDestroyed()) {
                return;
            }

            try {
                ERainAd.getInstance().populateNativeAdView(
                        act,
                        nativeAd,
                        adContainer,
                        shimmerView
                );
                mLog("AdManager====", "Ad Shown: " + adTag);
            } catch (Exception e) {
                e.printStackTrace();
                mLog("AdManager====", "Ad Not Available: " + adTag);
            }

        });


        // Optional: remove after showing (if one-time use)
//            nativeAdMap.remove(adTag);git push origin main:master

        return true;
    }

    // Clear Specific Ad
    public void clearAd(String adTag) {
//        nativeAdMap.remove(adTag);
    }

    // Clear All
    public void clearAllNative() {
        nativeAdMap.clear();
    }
}

