package gps.trackerid.location.adshelper;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApNativeAd;
import com.ads.module.funtion.AdCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

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
        Log.e("TAG", "AdManager:====loading for tag==" + adtag + "==if===" + adid);
        ERainAd.getInstance().loadNativeAdResultCallback(activity, adid, layout, new AdCallback() {
            @Override
            public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                super.onNativeAdLoaded(nativeAd);
                nativeAdMap.put(adtag, nativeAd);
                Log.e("AdManager=====onNativeAdLoaded===", adtag);
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                nativeAdMap.remove(adtag);
                Log.e("AdManager=====", "onAdFailedToLoad:=== " + i.getMessage());
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                Log.e("AdManager=====", "onAdFailedToShow:=== " + adError.getMessage());

                nativeAdMap.remove(adtag);

            }
        });
    }

    // Show Ad if available
    public boolean showNativeAdIfAvailable(Activity activity, String adTag, FrameLayout adContainer, ShimmerFrameLayout shimmerView) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return false;
        }
        ApNativeAd nativeAd = nativeAdMap.get(adTag);
        if (nativeAd == null) {
            return false;
        }
        if (nativeAd != null) {
            ERainAd.getInstance().populateNativeAdView(
                    activity,
                    nativeAd,
                    adContainer,
                    shimmerView
            );
            Log.e("AdManager====", "Ad Shown: " + adTag);

            // Optional: remove after showing (if one-time use)
//            nativeAdMap.remove(adTag);

            return true;
        }

        Log.e("AdManager====", "Ad Not Available: " + adTag);
        return false;
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

