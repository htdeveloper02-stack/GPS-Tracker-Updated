package gps.trackerid.location.adshelper;

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
}
