package gps.trackerid.location.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.Settings;

import androidx.core.view.ViewCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Random;
import java.util.UUID;

public class TrackingCodeUtil {

    private static final String PREF_NAME = "gps_trackers_prefs";
    private static final String KEY_CODE = "randomRegistrationCode";

    public static String getOrCreateTrackingCode(Activity context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String code = prefs.getString(KEY_CODE, "");

        if (code == null || code.isEmpty()) {
            code = getPhoneUniqueId(context);
            prefs.edit().putString(KEY_CODE, code).apply();
        }

        return code;
    }

    private static String generateTrackingCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    /**
     * Get a unique phone ID. If android_id is not available, generate a random alphanumeric code.
     */
    public static String getPhoneUniqueId(Activity activity) {
        if (activity == null) throw new IllegalArgumentException("activity must not be null");
        String androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId != null ? androidId : generateRandomAlphaNumericCode(20);
    }

    /**
     * Generate a random alphanumeric code of specified length.
     */
    public static String generateRandomAlphaNumericCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a QR code bitmap from the given text. Default size is 512 if not specified.
     */
    public Bitmap generateQrCode(String text) {
        return generateQrCode(text, 512);
    }

    /**
     * Generate a QR code bitmap from the given text and size.
     */
    public Bitmap generateQrCode(String text, int size) {
        if (text == null) throw new IllegalArgumentException("text must not be null");

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? ViewCompat.MEASURED_STATE_MASK : -1);
                }
            }

            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

