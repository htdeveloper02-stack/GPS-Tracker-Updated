package gps.trackerid.location.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class QrHelper {

    public static Bitmap generateQrCode(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y,
                            bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            // If model already includes manufacturer, just capitalize the model
            if (model.length() > 0) {
                return Character.toUpperCase(model.charAt(0)) + model.substring(1);
            } else {
                return model;
            }
        } else {
            // Capitalize manufacturer
            if (manufacturer.length() > 0) {
                manufacturer = Character.toUpperCase(manufacturer.charAt(0)) + manufacturer.substring(1);
            }
            // Return "Manufacturer Model"
            return manufacturer + " " + model;
        }
    }

}

