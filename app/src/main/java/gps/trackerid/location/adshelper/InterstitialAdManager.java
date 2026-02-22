package gps.trackerid.location.adshelper;

<<<<<<< HEAD
import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApInterstitialAd;
import com.ads.module.funtion.AdCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
public class InterstitialAdManager {

    private static long lastAdShownTime = 0;
    private static final long AD_INTERVAL = 28_000; // 30 seconds

    public static boolean canShowAd() {

        long currentTime = System.currentTimeMillis();

        return (currentTime - lastAdShownTime) > AD_INTERVAL;
    }

    public static void onAdShown() {
        lastAdShownTime = System.currentTimeMillis();
    }
<<<<<<< HEAD

    private static final Map<String, ApInterstitialAd> adMap = new HashMap<>();
    private static final Set<String> loadingAds = new HashSet<>();

    // ==============================
    // PRELOAD
    // ==============================
    public static synchronized void preload(Activity activity, String adId, String adTag) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        if (adMap.containsKey(adTag) || loadingAds.contains(adTag)) {
            return;
        }

        loadingAds.add(adTag);

        ERainAd.getInstance().getInterstitialAds(
                activity,
                adId,
                new AdCallback() {

                    @Override
                    public void onApInterstitialLoad(
                            @Nullable ApInterstitialAd ad) {

                        loadingAds.remove(adTag);

                        if (ad != null) {
                            adMap.put(adTag, ad);
                            Log.e("AdManager=====", "Preloaded: " + adTag);
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(
                            @Nullable LoadAdError error) {

                        loadingAds.remove(adTag);
                        adMap.remove(adTag);
                        Log.e("AdManager====", "Preload failed: " + adTag);
                    }
                });
    }

    // ==============================
    // SHOW
    // ==============================
    public static void showIfReady(Activity activity, String adTag, AdsConfig.MyCallback callback) {

        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {

            callback.callbackCall();
            return;
        }

        if (!InterstitialAdManager.canShowAd()) {
            callback.callbackCall();
            return;
        }
        Log.e("TAG", "AdManager:===inter show tag==="+adTag );
        ApInterstitialAd ad = adMap.get(adTag);

        if (ad != null) {

            ERainAd.getInstance().forceShowInterstitial(
                    activity,
                    ad,
                    new AdCallback() {

                        @Override
                        public void onNextAction() {
                        }

                        @Override
                        public void onAdClosed() {
//                            clear(adTag);
                            InterstitialAdManager.onAdShown();
                            callback.callbackCall();

                            // preload next
//                            preload(activity.getApplicationContext(), adTag);
                        }

                        @Override
                        public void onAdFailedToShow(@Nullable AdError adError) {
//                            clear(adTag);
                            InterstitialAdManager.onAdShown();
                            callback.callbackCall();

//                            preload(activity.getApplicationContext(), adTag);
                        }
                    },
                    true
            );

        } else {
            // Not ready → continue app flow
            callback.callbackCall();
            // Preload for next time
//            preload(activity.getApplicationContext(), adTag);
        }
    }

    // ==============================
    // CLEAR
    // ==============================
    public static void clear(String adTag) {
//        adMap.remove(adTag);
    }

    public static void clearAll() {
        for (String tag : adMap.keySet()) {
            clear(tag);
        }
        adMap.clear();
    }


=======
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
}
