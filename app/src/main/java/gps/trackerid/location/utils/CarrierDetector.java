package gps.trackerid.location.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import gps.trackerid.location.models.PhoneCarrierInfo;

public final class CarrierDetector {

    private static final String TAG = "CarrierDetector";

    private static final Map<String, PhoneCarrierInfo> PREFIX_MAP = new LinkedHashMap<>();
    private static boolean isLoaded = false;

    private CarrierDetector() {
    }

    /**
     * Load phone prefix data from assets/phone_data.json
     */
    public static synchronized void loadFromAssets(Context context) {
        if (isLoaded) return;

        try {
            InputStream inputStream = context.getAssets().open("phone_data.json");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            reader.close();

            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String prefix = obj.getString("prefix");
                String country = obj.getString("country");
                String carrier = obj.getString("carrier");

                PREFIX_MAP.put(prefix, new PhoneCarrierInfo(country, carrier));
            }

            isLoaded = true;
            Log.d(TAG, "Loaded " + PREFIX_MAP.size() + " prefixes from assets");

        } catch (Exception e) {
            Log.e(TAG, "Failed to load phone_data.json", e);
        }
    }

    /**
     * Detect carrier using prefix map
     */
    public static PhoneCarrierInfo detect(Context context, String phoneNumber) {
        loadFromAssets(context);

        if (phoneNumber == null) return null;

        String digits = phoneNumber.replaceAll("[^0-9]", "");
        Log.d(TAG, "Cleaned number: " + digits);

        for (int len = 6; len >= 3; len--) {
            if (digits.length() >= len) {
                String prefix = digits.substring(0, len);
                PhoneCarrierInfo info = PREFIX_MAP.get(prefix);
                if (info != null) {
                    return info;
                }
            }
        }
        return null;
    }
}
