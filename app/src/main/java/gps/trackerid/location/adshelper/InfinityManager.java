/*
package gps.trackerid.location.adshelper;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApNativeAd;
import com.ads.module.funtion.AdCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

public class InfinityManager {
    private static Map<String, ApNativeAd> currentAds = new HashMap<>();
//    StateFlow<Map<String, NativeUIStart>> _adStateFlow =new NativeUIStart(new HashMap<>());

    private static final MutableStateFlow<Map<String, NativeUIStart>> _adStateFlow = StateFlowKt.MutableStateFlow(new HashMap<>());

    public static StateFlow<Map<String, NativeUIStart>> getAdStateFlow() {
        return _adStateFlow;
    }

    public static void mLoadAds(Activity activity, String adid, int layout, String adtag) {
        Log.e("TAG", "mLoadAds:====loading for tag==" + adtag + "==if===" + adid);
            ERainAd.getInstance().loadNativeAdResultCallback(activity, adid, layout, new AdCallback() {
                @Override
                public void onNativeAdLoaded(@NonNull ApNativeAd nativeAd) {
                    super.onNativeAdLoaded(nativeAd);
                    Log.e("TAG", "mLoadAds==onNativeAdLoaded:====");
                    currentAds.put(adtag, nativeAd);
                    updateAdState(adtag, new NativeUIStart.Success(nativeAd, adtag, adid));
                }

                @Override
                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                    super.onAdFailedToLoad(i);
                    Log.e("TAG", "mLoadAds==oonAdFailedToLoad:====" + i.getResponseInfo().toString());
                    currentAds.put(adtag, null);
                    updateAdState(adtag, new NativeUIStart.Failed(adtag, adid, i.getMessage()));
                }

                @Override
                public void onAdFailedToShow(@Nullable AdError adError) {
                    super.onAdFailedToShow(adError);
                    Log.e("TAG", "mLoadAds==oonAdFailedToLoad:====" + adError.getMessage());
                    currentAds.put(adtag, null);
                    updateAdState(adtag, new NativeUIStart.Failed(adtag, adid, adError.getMessage()));
                }
            });
    }

//    private static void updateAdState(String tagName, NativeUIStart state) {
//
//        Map<String, NativeUIStart> currentMap =
//                new HashMap<>(_adStateFlow.getValue());
//
//        currentMap.put(tagName, state);
//
//        _adStateFlow.setValue(currentMap);
//    }
    private static void updateAdState(String tagName, NativeUIStart state) {

        Map<String, NativeUIStart> currentMap =
                new HashMap<>(_adStateFlow.getValue());

        currentMap.put(tagName, state);

        _adStateFlow.setValue(currentMap);

        // 🔥 notify Java listeners
        for (AdStateListener listener : listeners) {
            listener.onAdStateChanged(currentMap);
        }
    }

    public interface AdStateListener {
        void onAdStateChanged(Map<String, NativeUIStart> states);
    }
    private static final List<AdStateListener> listeners = new ArrayList<>();

    public static void addListener(AdStateListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(AdStateListener listener) {
        listeners.remove(listener);
    }

}
*/
