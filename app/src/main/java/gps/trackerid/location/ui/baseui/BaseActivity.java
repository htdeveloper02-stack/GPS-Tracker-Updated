package gps.trackerid.location.ui.baseui;

import static gps.trackerid.location.utils.Global.mLog;
import static gps.trackerid.location.utils.Global.showInternetDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ads.module.admob.AppOpenManager;
import com.ads.module.ads.ERainAd;
import com.ads.module.util.Preference;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.ui.NearByActivity;
import gps.trackerid.location.ui.PhoneLocator;
import gps.trackerid.location.ui.SplashActivity;
import gps.trackerid.location.ui.TrafficAlertActivity;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.LocaleHelper;
import gps.trackerid.location.utils.SystmeUtils;


public class BaseActivity extends AppCompatActivity {
    Preference preference;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    protected RemoteConfigListener remoteConfigListener;

    public void setRemoteConfigListener(RemoteConfigListener listener) {
        this.remoteConfigListener = listener;
    }

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
     * @param languageCode - e.g., "en", "es", "fr"
     */
    public void changeAppLanguage(String languageCode) {
        preference.saveLanguage(languageCode);
        LocaleHelper.setLocale(this, languageCode);
        recreate(); // restart activity to apply language
    }

    public interface RemoteConfigListener {
        void onRemoteConfigLoaded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.dismissInternetDialog(BaseActivity.this);
        if (!Global.iSGetAds) {
            mCallRemoteConfigData();
        }
        if (Global.ISLanguageChange) {
            if (ERainAd.getInstance().getShouldDisplayWidgetUninstall()) {
                initShortCut();
            }
        }
    }

    public void mCallRemoteConfigData() {
        if (Global.isInternetConnected(this)) {

            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1).build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            try {
                                Global.iSGetAds = true;
                                AdsConfig.mSetRemoteData(mFirebaseRemoteConfig);
                                if (Global.open_resume) {
                                    AppOpenManager.getInstance().enableAppResume();
                                } else {
                                    AppOpenManager.getInstance().disableAppResume();
                                }
                                if (remoteConfigListener != null) {
                                    remoteConfigListener.onRemoteConfigLoaded();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            mLog("RemoteConfig", "Fetch failed");

                        }
                    });
        } else {
            showInternetDialog(BaseActivity.this);
        }

    }

    private void initShortCut() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return;

        try {
            ShortcutManager shortcutManager =
                    (ShortcutManager) getSystemService(ShortcutManager.class);

            if (shortcutManager == null) return;

            shortcutManager.removeAllDynamicShortcuts();

            ArrayList<ShortcutInfo> shortcuts = new ArrayList<>();


            shortcuts.add(
                    new ShortcutInfo.Builder(this, "shortcut_locator")
                            .setShortLabel(getString(R.string.phonelocator))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_locator))
                            .setIntent(createShortcutIntent(
                                    PhoneLocator.class,
                                    "android.intent.action.SHORTCUT_PHONE_LOCATOR",
                                    Global.ACTION_OPEN_LOCATOR))
                            .setRank(0)
                            .build()
            );
            shortcuts.add(
                    new ShortcutInfo.Builder(this, "hortcut_traffic")
                            .setShortLabel(getString(R.string.traffic))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_traffic))
                            .setIntent(createShortcutIntent(
                                    TrafficAlertActivity.class,
                                    "android.intent.action.SHORTCUT_TRAFFIC_ALERT",
                                    Global.ACTION_OPEN_HOME))
                            .setRank(1)
                            .build()
            );
            shortcuts.add(
                    new ShortcutInfo.Builder(this, "shortcut_nearby")
                            .setShortLabel(getString(R.string.nearby))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_nearby))
                            .setIntent(createShortcutIntent(
                                    NearByActivity.class,
                                    "android.intent.action.SHORTCUT_NEAR_BY",
                                    Global.ACTION_OPEN_HOME))
                            .setRank(2)
                            .build()
            );


            shortcuts.add(
                    new ShortcutInfo.Builder(this, "shortcut_uninstall")
                            .setShortLabel(getString(R.string.txt_uninstall))
                            .setIcon(Icon.createWithResource(
                                    this, R.drawable.ic_shortcut_uninstall))
                            .setIntent(createShortcutIntent(
                                    SplashActivity.class,
                                    "android.intent.action.SHORTCUT_UNINSTALL_APP",
                                    Global.ACTION_OPEN_UNINSTALL))
                            .setRank(3)
                            .build()
            );

            shortcutManager.setDynamicShortcuts(shortcuts);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Intent createShortcutIntent(
            Class<?> target,
            String action,
            String shortcutType) {

        Intent intent = new Intent(this, target);
        intent.setAction(action);
        intent.putExtra(Global.FROM_SHORTCUT, shortcutType);

        // Clears task properly
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        return intent;
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

