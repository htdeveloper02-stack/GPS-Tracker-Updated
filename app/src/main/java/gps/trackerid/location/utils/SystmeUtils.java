package gps.trackerid.location.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

public class SystmeUtils {
//    public static void enableFullScreenUi(Activity activity, View view) {
//        if (activity == null || view == null) {
//            throw new IllegalArgumentException("Activity and View must not be null");
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API 30+
//            WindowInsetsController insetsController = activity.getWindow().getInsetsController();
//            if (insetsController != null) {
//                // Hide status and navigation bars
//                insetsController.hide( WindowInsets.Type.navigationBars());
//                // Allow bars to be revealed with swipe gestures
//                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
//            }
//            // Consume all window insets to avoid resizing
//            view.setOnApplyWindowInsetsListener((v, insets) -> WindowInsets.CONSUMED);
//        } else {
//            // For older versions, use deprecated system UI flags
//            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
//
//            // Optional: consume insets if needed (no direct equivalent pre-API 30)
//        }
//    }

    public static void enableFullScreenUi(Activity activity, View view) {

        if (activity == null) return;

        Window window = activity.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            window.setDecorFitsSystemWindows(false);

            WindowInsetsController controller = window.getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.navigationBars());

                controller.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
            }

        } else {

            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

}
