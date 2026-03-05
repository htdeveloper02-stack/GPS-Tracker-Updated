package gps.trackerid.location.ui.baseui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import gps.trackerid.location.ads.Preference;
import gps.trackerid.location.ads.ShortcutUtils;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.LocaleHelper;
import gps.trackerid.location.utils.SystmeUtils;


public class BaseActivity extends AppCompatActivity {
    Preference preference;

    @Override
    protected void attachBaseContext(Context newBase) {
        // Get saved language or default to English
        preference = new Preference(newBase);
        String lang = preference.getSavedLanguage();
        LocaleHelper.setLocale(newBase, lang);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call the method to enable fullscreen UI
        final View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.post(() -> SystmeUtils.enableFullScreenUi(this, rootView));
        }

    }

    /**
     * Call this method to change language dynamically
     *
     * @param languageCode - e.g., "en", "es", "fr"
     */
    public void changeAppLanguage(String languageCode) {
        preference.saveLanguage(languageCode);
        LocaleHelper.setLocale(this, languageCode);
        recreate(); // restart activity to apply language
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.dismissInternetDialog(BaseActivity.this);

        if (Global.ISLanguageChange) {
            ShortcutUtils.INSTANCE.checkInit(this);
        }
    }

    /**
     * Draw content behind the status bar by disabling fitSystemWindows.
     * Works on all API levels.
     */
    public static void drawBehindStatusBar(Activity activity, View view) {
        if (activity == null || view == null) {
            throw new IllegalArgumentException("Activity and View must not be null");
        }

        // Disable fitting system windows so content can be drawn behind system bars
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);

        // Consume insets to avoid automatic padding/margin adjustments
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> WindowInsetsCompat.CONSUMED);
    }
}

