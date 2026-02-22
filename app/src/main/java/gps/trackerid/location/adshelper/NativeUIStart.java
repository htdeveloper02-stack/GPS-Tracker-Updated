package gps.trackerid.location.adshelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.module.ads.wrapper.ApNativeAd;

public class NativeUIStart {
    private final String tagName;
    private final String adId;

    protected NativeUIStart(String tagName, String adId) {
        this.tagName = tagName;
        this.adId = adId;
    }

    public String getTagName() {
        return tagName;
    }

    public String getAdId() {
        return adId;
    }

    // -----------------------
    // SUCCESS STATE
    // -----------------------
    public static class Success extends NativeUIStart {

        private final ApNativeAd nativeAd;

        public Success(@NonNull ApNativeAd nativeAd,
                       @NonNull String tagName,
                       @NonNull String adId) {
            super(tagName, adId);
            this.nativeAd = nativeAd;
        }

        public ApNativeAd getNativeAd() {
            return nativeAd;
        }
    }

    // -----------------------
    // FAILED STATE
    // -----------------------
    public static class Failed extends NativeUIStart {

        private final String error;

        public Failed(@NonNull String tagName,
                      @NonNull String adId,
                      @Nullable String error) {
            super(tagName, adId);
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    // -----------------------
    // LOADING STATE (Optional)
    // -----------------------
    public static class Loading extends NativeUIStart {

        public Loading(@NonNull String tagName,
                       @NonNull String adId) {
            super(tagName, adId);
        }
    }

    // -----------------------
    // Factory Methods
    // -----------------------

    public static NativeUIStart success(
            @NonNull ApNativeAd nativeAd,
            @NonNull String tagName,
            @NonNull String adId
    ) {
        return new Success(nativeAd, tagName, adId);
    }

    public static NativeUIStart failed(
            @NonNull String tagName,
            @NonNull String adId,
            @Nullable String error
    ) {
        return new Failed(tagName, adId, error);
    }

    public static NativeUIStart loading(
            @NonNull String tagName,
            @NonNull String adId
    ) {
        return new Loading(tagName, adId);
    }


}
