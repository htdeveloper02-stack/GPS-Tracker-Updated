package gps.trackerid.location.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

import gps.trackerid.location.R;

public class Global {
    public static boolean iSGetAds = false;
    public static boolean ISLanguageChange = false;

    public static final String KEY_TRACKING_SCREEN_FROM = "key_tracking_screen_from";
    public static final String ACTION_OPEN_HOME = "action_open_home";
    public static final String ACTION_OPEN_LOCATOR = "action_open_locator";
    public static final String FROM_SHORTCUT = "from_shortcut";
    public static final String ACTION_OPEN_UNINSTALL = "action_open_uninstall";
    public static String FREE_SERVERS = "";
    public static String privacy_policy = "";
    public static String about_us = "";
    public static String contact_us = "";
    public static String PRO_SERVERS = "";
    public static String ip = "";
    public static String API_KEY = "wbnMYs9bha0Geg.2yeegiZBJ3cUfjUPWrokcHDRqnjOhwEK2tH";
    public static boolean inter_splash;
    public static boolean banner_splash;

    public static boolean native_language_1;
    public static boolean native_language_1_click;
    public static boolean native_language_2;
    public static boolean native_language_2_click;

    public static boolean native_onboarding_1_1;
    public static boolean native_onboarding_2_1;
    public static boolean native_onboarding_1_4;
    public static boolean native_onboarding_2_4;

    public static boolean native_onboarding_fullscreen_1_2;
    public static boolean native_onboarding_fullscreen_2_2;


    public static boolean open_resume;
    public static boolean inter_onboarding;

    public static boolean inter_home;
    public static boolean native_home;
    public static boolean banner_collap_home;

    public static boolean inter_back;
    public static boolean banner_all;

    public static boolean native_phone_locator;
    public static boolean inter_splash_uninstall;
    public static boolean banner_splash_uninstall;
    public static boolean native_uninstall;
    public static boolean native_survey_uninstall;
    public static boolean native_setting;
    public static boolean delay_button_done_language;
    public static long height_button_cta = 40;

    public static boolean IsTaken(Activity activity, List<String> list, String str) {
        if (ContextCompat.checkSelfPermission(activity, str) != 0) {
            list.add(str);
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, str);
        }
        return true;
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    public static Dialog internetDialog;

    public static void showInternetDialog(Activity activity) {
        if (internetDialog != null && internetDialog.isShowing()) return;
        internetDialog = new Dialog(activity);
        internetDialog.setCancelable(false);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_net, null);
        internetDialog.setContentView(view);
        internetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView mIvOpenSetting = view.findViewById(R.id.mIvOpenSetting);

        mIvOpenSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                activity.startActivity(intent);
            }
        });

        internetDialog.show();
        // ✅ Apply full width after showing
        if (internetDialog.getWindow() != null) {
            internetDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public static void dismissInternetDialog(Activity activity) {
        if (activity != null &&
                !activity.isFinishing() &&
                !activity.isDestroyed() &&
                internetDialog != null &&
                internetDialog.isShowing()) {

            internetDialog.dismiss();
        }
//        if (internetDialog != null && internetDialog.isShowing()) {
//            internetDialog.dismiss();
//        }
    }

    public static boolean isFastClick() {
        return isFastClick(1000);
    }

    public static long lastClickTime;

    public static boolean isFastClick(long j) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < j) {
            return true;
        }
        lastClickTime = currentTimeMillis;
        return false;
    }
    public static void openChromeCustomTabUrl(Context activity, String url) {
        String str = url;
        if (!str.equalsIgnoreCase("")) {
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            intentBuilder.setToolbarColor(ContextCompat.getColor(activity, R.color.black));
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.black)); // better if you replace this with a darker shade
            intentBuilder.setStartAnimations(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            intentBuilder.setExitAnimations(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            CustomTabsIntent customTabsIntent = intentBuilder.build();
            try {
                customTabsIntent.launchUrl(activity, Uri.parse(str));
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(str));
                activity.startActivity(intent);
            }
        }
    }
}
