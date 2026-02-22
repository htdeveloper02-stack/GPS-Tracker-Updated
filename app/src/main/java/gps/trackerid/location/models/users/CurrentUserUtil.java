package gps.trackerid.location.models.users;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import gps.trackerid.location.utils.PreferenceUtil;

public class CurrentUserUtil {
    private static final String KEY_CURRENT_USER = "currentUser";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LAST_ADDRESS = "lastKnownAddress";
    private static final String KEY_LANGUAGGE = "language";

    private static final Gson gson = new Gson();

    private CurrentUserUtil() {
        // no instance
    }

    /* ===================== CURRENT USER ===================== */

    public static void saveCurrentUser(Context context, DataUser user) {
        if (context == null || user == null) return;

        SharedPreferences prefs = PreferenceUtil.getGpsTrackersPreferences(context);
        prefs.edit()
                .putString(KEY_CURRENT_USER, gson.toJson(user))
                .apply();
    }

    public static DataUser getCurrentUser(Context context) {
        if (context == null) return null;

        SharedPreferences prefs = PreferenceUtil.getGpsTrackersPreferences(context);
        String json = prefs.getString(KEY_CURRENT_USER, null);

        if (json == null || json.isEmpty()) return null;

        try {
            return gson.fromJson(json, DataUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    /* ===================== PHONE ===================== */

    public static void setPhone(Context context, String phone) {
        if (context == null || phone == null) return;

        PreferenceUtil.getGpsTrackersPreferences(context)
                .edit()
                .putString(KEY_PHONE, phone)
                .apply();
    }

    public static String getPhone(Context context) {
        if (context == null) return "";

        return PreferenceUtil.getGpsTrackersPreferences(context)
                .getString(KEY_PHONE, "");
    }

    /* ===================== LAST KNOWN ADDRESS ===================== */

    public static void setLastKnownAddress(Context context, String address) {
        if (context == null || address == null) return;

        PreferenceUtil.getGpsTrackersPreferences(context)
                .edit()
                .putString(KEY_LAST_ADDRESS, address)
                .apply();
    }

    public static void setLanguage(Context context, String language) {
        if (context == null || language == null) return;

        PreferenceUtil.getGpsTrackersPreferences(context)
                .edit()
                .putString(KEY_LANGUAGGE, language)
                .apply();
    }

    public static String getLastKnownAddress(Context context) {
        if (context == null) return "";

        return PreferenceUtil.getGpsTrackersPreferences(context)
                .getString(KEY_LAST_ADDRESS, "");
    }
    public static String getLanguage(Context context) {
        if (context == null) return "";

        return PreferenceUtil.getGpsTrackersPreferences(context)
                .getString(KEY_LANGUAGGE, "English");
    }
}
