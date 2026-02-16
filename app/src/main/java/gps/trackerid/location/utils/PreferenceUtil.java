package gps.trackerid.location.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferenceUtil {

    private static final String PREF_NAME = "gps_tracker_prefs";

    private PreferenceUtil() {
        // no instance
    }

    public static SharedPreferences getGpsTrackersPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
