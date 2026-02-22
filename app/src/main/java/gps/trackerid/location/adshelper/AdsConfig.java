package gps.trackerid.location.adshelper;

import static android.view.View.GONE;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

<<<<<<< HEAD
import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApInterstitialAd;
import com.ads.module.funtion.AdCallback;
=======
import androidx.annotation.Nullable;

import com.ads.module.admob.Admob;
import com.ads.module.ads.ERainAd;
import com.ads.module.ads.wrapper.ApInterstitialAd;
import com.ads.module.funtion.AdCallback;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import gps.trackerid.location.utils.Global;

public class AdsConfig {
    private static final boolean IS_TEST = false;

    public static String getInterSplash() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-2864154863223892/1887558354";
    }

    public static String getInterOnboarding() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-2864154863223892/2754967211";
    }

    public static String getInterHome() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-2864154863223892/7742520266";
    }

    public static String getInterBack() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-2864154863223892/6429438590";
    }

    public static String getAppOpenResume() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/9257395921"
                : "ca-app-pub-2864154863223892/9055601933";
    }

    public static String getBannerSplash() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/9214589741"
                : "ca-app-pub-2864154863223892/9574476683";
    }

    public static String getBannerHomeCollapse() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/9214589741"
                : "ca-app-pub-2864154863223892/9414694589";
    }

    public static String getBannerAll() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/9214589741"
                : "ca-app-pub-2864154863223892/5443659987";
    }

    public static String getNativeLanguage1() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/1481002276";
    }

    public static String getNativeLanguage1Click() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/9167920602";
    }

    public static String getNativeLanguage2() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/4186418638";
    }

    public static String getNativeLanguage2Click() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/3349863581";
    }

    public static String getNativeOnboarding11() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/5213609593";
    }

    public static String getNativeOnboarding21() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/9247173622";
    }

    public static String getNativeOnboarding14() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/5667021269";
    }

    public static String getNativeOnboarding24() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/4353939592";
    }

    public static String getNativeOnboardingFullscreen12() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/3760927121";
    }

    public static String getNativeOnboardingFullscreen22() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/9382904998";
    }

    public static String getNativeHome() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/6195518772";
    }

    public static String getNativePhoneLocator() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/9128803875";
    }

    public static String getInterSplashUninstall() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/1033173712"
                : "ca-app-pub-2864154863223892/4162367906";
    }

    public static String getBannerSplashUninstall() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/9214589741"
                : "ca-app-pub-2864154863223892/9606333485";
    }

    public static String getNativeUninstall() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/5220258110";
    }

    public static String getNativeSurveyUninstall() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/5615409167";
    }

    public static String getNativeSetting() {
        return IS_TEST
                ? "ca-app-pub-3940256099942544/2247696110"
                : "ca-app-pub-2864154863223892/1999597789";
    }

    public static void mSetRemoteData(FirebaseRemoteConfig mFirebaseRemoteConfig) {
        try {
            Global.privacy_policy = mFirebaseRemoteConfig.getAll().get("privacy_policy").asString();
            Global.about_us = mFirebaseRemoteConfig.getAll().get("about_us").asString();
            Global.contact_us = mFirebaseRemoteConfig.getAll().get("contact_us").asString();
            Global.inter_splash =
                    mFirebaseRemoteConfig.getBoolean("inter_splash");

            Global.banner_splash =
                    mFirebaseRemoteConfig.getBoolean("banner_splash");

            Global.native_language_1 = mFirebaseRemoteConfig.getBoolean("native_language_1");
            Global.native_language_1_click = mFirebaseRemoteConfig.getBoolean("native_language_1_click");
            Global.native_language_2 = mFirebaseRemoteConfig.getBoolean("native_language_2");
            Global.native_language_2_click = mFirebaseRemoteConfig.getBoolean("native_language_2_click");

            Global.native_onboarding_1_1 = mFirebaseRemoteConfig.getBoolean("native_onboarding_1_1");
            Global.native_onboarding_2_1 = mFirebaseRemoteConfig.getBoolean("native_onboarding_2_1");
            Global.native_onboarding_1_4 = mFirebaseRemoteConfig.getBoolean("native_onboarding_1_4");
            Global.native_onboarding_2_4 = mFirebaseRemoteConfig.getBoolean("native_onboarding_2_4");

            Global.native_onboarding_fullscreen_1_2 =
                    mFirebaseRemoteConfig.getBoolean("native_onboarding_fullscreen_1_2");

            Global.native_onboarding_fullscreen_2_2 =
                    mFirebaseRemoteConfig.getBoolean("native_onboarding_fullscreen_2_2");


            Global.open_resume =
                    mFirebaseRemoteConfig.getBoolean("open_resume");

            Global.inter_onboarding =
                    mFirebaseRemoteConfig.getBoolean("inter_onboarding");

            Global.inter_home =
                    mFirebaseRemoteConfig.getBoolean("inter_home");

            Global.native_home =
                    mFirebaseRemoteConfig.getBoolean("native_home");

            Global.banner_collap_home =
                    mFirebaseRemoteConfig.getBoolean("banner_collap_home");

            Global.inter_back =
                    mFirebaseRemoteConfig.getBoolean("inter_back");

            Global.banner_all =
                    mFirebaseRemoteConfig.getBoolean("banner_all");

            Global.native_phone_locator =
                    mFirebaseRemoteConfig.getBoolean("native_phone_locator");

            Global.inter_splash_uninstall =
                    mFirebaseRemoteConfig.getBoolean("inter_splash_uninstall");

            Global.banner_splash_uninstall =
                    mFirebaseRemoteConfig.getBoolean("banner_splash_uninstall");

            Global.native_uninstall =
                    mFirebaseRemoteConfig.getBoolean("native_uninstall");

            Global.native_survey_uninstall =
                    mFirebaseRemoteConfig.getBoolean("native_survey_uninstall");

            Global.native_setting =
                    mFirebaseRemoteConfig.getBoolean("native_setting");
            Global.delay_button_done_language =
                    mFirebaseRemoteConfig.getBoolean("delay_button_done_language");
            Global.height_button_cta = (int) mFirebaseRemoteConfig.getLong("height_button_cta");
            Log.e("TAG", "remotedata:=====Donee..");
        } catch (Exception e) {
            Log.e("TAG", "remotedata:=====" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isShowNative(boolean nativeLanguage, FrameLayout frAds) {
        if (!nativeLanguage) {
            frAds.setVisibility(GONE);
            return false;
        }
        return true;
    }

    public interface MyCallback {
        void callbackCall();
    }

    private static ApInterstitialAd mInterstitialAd, mHomeInterstitialAd, mBackInterstitialAd;

    public static void loadSplashInterstitialAds(Activity activity, MyCallback myCallback) {
        if (Global.inter_splash) {
            ERainAd.getInstance().loadSplashInterstitialAds(activity, getInterSplash(), 25000, 5000, new AdCallback() {
                @Override
                public void onNextAction() {
                    super.onNextAction();
                    myCallback.callbackCall();
                }
            });
        } else {
            myCallback.callbackCall();
        }
    }

    public static void loadUnsintallInterstitialAds(Activity activity, MyCallback myCallback) {
        if (Global.inter_splash_uninstall && Global.isInternetConnected(activity)) {
            ERainAd.getInstance().loadSplashInterstitialAds(activity, getInterSplashUninstall(), 25000, 5000, new AdCallback() {
                @Override
                public void onNextAction() {
                    super.onNextAction();
                    myCallback.callbackCall();
                }
            });
        } else {
            myCallback.callbackCall();
        }
    }

<<<<<<< HEAD
//    public static void loadInterstitialAds(Activity activity, String adsId, MyCallback myCallback) {
//        if (Global.inter_onboarding && ERainAd.getInstance().getShouldDisplayInterOnboarding() && InterstitialAdManager.canShowAd() && Global.isInternetConnected(activity)) {
//            Admob.getInstance().showDialog(activity);
//            if (mInterstitialAd == null) {
//                mShowIntestitial(activity, adsId, myCallback);
//            } else {
//                ERainAd.getInstance().forceShowInterstitial(activity, mInterstitialAd, new AdCallback() {
//                    @Override
//                    public void onNextAction() {
//                        super.onNextAction();
//                        InterstitialAdManager.onAdShown();
//                    }
//
//                    @Override
//                    public void onAdClosed() {
//                        super.onAdClosed();
//                        myCallback.callbackCall();
//                    }
//
//                    @Override
//                    public void onAdFailedToShow(@Nullable AdError adError) {
//                        super.onAdFailedToShow(adError);
//                        myCallback.callbackCall();
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
//                        super.onAdFailedToLoad(i);
//                        myCallback.callbackCall();
//                    }
//                }, true);
//
//            }
//        } else {
//            myCallback.callbackCall();
//        }
//
//    }
//
//    private static void mShowIntestitial(Activity activity, String adsId, MyCallback myCallback) {
//        ERainAd.getInstance().getInterstitialAds(activity, adsId, new AdCallback() {
//            @Override
//            public void onApInterstitialLoad(@Nullable ApInterstitialAd apInterstitialAd) {
//                super.onApInterstitialLoad(apInterstitialAd);
//                mInterstitialAd = apInterstitialAd;
//                ERainAd.getInstance().forceShowInterstitial(activity, mInterstitialAd, new AdCallback() {
//                    @Override
//                    public void onNextAction() {
//                        super.onNextAction();
//                        InterstitialAdManager.onAdShown();
//                    }
//
//                    @Override
//                    public void onAdClosed() {
//                        super.onAdClosed();
//                        myCallback.callbackCall();
//                    }
//
//                    @Override
//                    public void onAdFailedToShow(@Nullable AdError adError) {
//                        super.onAdFailedToShow(adError);
//                        myCallback.callbackCall();
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
//                        super.onAdFailedToLoad(i);
//                        myCallback.callbackCall();
//                    }
//                }, true);
//
//            }
//
//            @Override
//            public void onAdFailedToShow(@Nullable AdError adError) {
//                super.onAdFailedToShow(adError);
//                myCallback.callbackCall();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@Nullable LoadAdError i) {
//                super.onAdFailedToLoad(i);
//                myCallback.callbackCall();
//            }
//        });
//    }

//    public static void loadHomeInterstitialAds(Activity activity, MyCallback myCallback) {
//        if (Global.inter_home && InterstitialAdManager.canShowAd() && Global.isInternetConnected(activity)) {
//            Admob.getInstance().showDialog(activity);
//            if (mHomeInterstitialAd == null) {
//                mShowHomeInterstitial(activity, myCallback);
//            } else {
//                ERainAd.getInstance().forceShowInterstitial(activity, mHomeInterstitialAd, new AdCallback() {
//                    @Override
//                    public void onNextAction() {
//                        super.onNextAction();
//                        myCallback.callbackCall();
//                        InterstitialAdManager.onAdShown();
//                    }
//                }, true);
//            }
//        } else {
//            myCallback.callbackCall();
//        }
//    }
//
//    private static void mShowHomeInterstitial(Activity activity, MyCallback myCallback) {
//        ERainAd.getInstance().getInterstitialAds(activity, getInterHome(), new AdCallback() {
//            @Override
//            public void onApInterstitialLoad(@Nullable ApInterstitialAd apInterstitialAd) {
//                super.onApInterstitialLoad(apInterstitialAd);
//                mHomeInterstitialAd = apInterstitialAd;
//                ERainAd.getInstance().forceShowInterstitial(activity, mHomeInterstitialAd, new AdCallback() {
//                    @Override
//                    public void onNextAction() {
//                        super.onNextAction();
//                        myCallback.callbackCall();
//                        InterstitialAdManager.onAdShown();
//                    }
//                }, true);
//            }
//        });
//    }
//
//    public static void loadBAckInterstitialAds(Activity activity, MyCallback myCallback) {
//        if (Global.inter_back && InterstitialAdManager.canShowAd() && Global.isInternetConnected(activity)) {
//            Admob.getInstance().showDialog(activity);
//            if (mBackInterstitialAd == null) {
//                ShowBAckInterstitial(activity, myCallback);
//            } else {
//                ERainAd.getInstance().forceShowInterstitial(activity, mBackInterstitialAd, new AdCallback() {
//                    @Override
//                    public void onNextAction() {
//                        super.onNextAction();
//                        myCallback.callbackCall();
//                        InterstitialAdManager.onAdShown();
//                    }
//                }, true);
//            }
//        } else {
//            myCallback.callbackCall();
//        }
//    }
//
//    private static void ShowBAckInterstitial(Activity activity, MyCallback myCallback) {
//        ERainAd.getInstance().getInterstitialAds(activity, getInterBack(), new AdCallback() {
//            @Override
//            public void onApInterstitialLoad(@Nullable ApInterstitialAd apInterstitialAd) {
//                super.onApInterstitialLoad(apInterstitialAd);
//                mBackInterstitialAd = apInterstitialAd;
//                ERainAd.getInstance().forceShowInterstitial(activity, mBackInterstitialAd, new AdCallback() {
//                    @Override
//                    public void onNextAction() {
//                        super.onNextAction();
//                        myCallback.callbackCall();
//                        InterstitialAdManager.onAdShown();
//                    }
//                }, true);
//            }
//        });
//    }
=======
    public static void loadInterstitialAds(Activity activity, String adsId, MyCallback myCallback) {
        if (Global.inter_onboarding && ERainAd.getInstance().getShouldDisplayInterOnboarding() && InterstitialAdManager.canShowAd() && Global.isInternetConnected(activity)) {
            Admob.getInstance().showDialog(activity);
            if (mInterstitialAd == null) {
                mShowIntestitial(activity, adsId, myCallback);
            } else {
                ERainAd.getInstance().forceShowInterstitial(activity, mInterstitialAd, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        InterstitialAdManager.onAdShown();
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        myCallback.callbackCall();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        myCallback.callbackCall();
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        myCallback.callbackCall();
                    }
                }, true);

            }
        } else {
            myCallback.callbackCall();
        }

    }

    private static void mShowIntestitial(Activity activity, String adsId, MyCallback myCallback) {
        ERainAd.getInstance().getInterstitialAds(activity, adsId, new AdCallback() {
            @Override
            public void onApInterstitialLoad(@Nullable ApInterstitialAd apInterstitialAd) {
                super.onApInterstitialLoad(apInterstitialAd);
                mInterstitialAd = apInterstitialAd;
                ERainAd.getInstance().forceShowInterstitial(activity, mInterstitialAd, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        InterstitialAdManager.onAdShown();
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        myCallback.callbackCall();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        myCallback.callbackCall();
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        myCallback.callbackCall();
                    }
                }, true);

            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                myCallback.callbackCall();
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                myCallback.callbackCall();
            }
        });
    }

    public static void loadHomeInterstitialAds(Activity activity, MyCallback myCallback) {
        if (Global.inter_home && InterstitialAdManager.canShowAd() && Global.isInternetConnected(activity)) {
            Admob.getInstance().showDialog(activity);
            if (mHomeInterstitialAd == null) {
                mShowHomeInterstitial(activity, myCallback);
            } else {
                ERainAd.getInstance().forceShowInterstitial(activity, mHomeInterstitialAd, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        myCallback.callbackCall();
                        InterstitialAdManager.onAdShown();
                    }
                }, true);
            }
        } else {
            myCallback.callbackCall();
        }
    }

    private static void mShowHomeInterstitial(Activity activity, MyCallback myCallback) {
        ERainAd.getInstance().getInterstitialAds(activity, getInterHome(), new AdCallback() {
            @Override
            public void onApInterstitialLoad(@Nullable ApInterstitialAd apInterstitialAd) {
                super.onApInterstitialLoad(apInterstitialAd);
                mHomeInterstitialAd = apInterstitialAd;
                ERainAd.getInstance().forceShowInterstitial(activity, mHomeInterstitialAd, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        myCallback.callbackCall();
                        InterstitialAdManager.onAdShown();
                    }
                }, true);
            }
        });
    }

    public static void loadBAckInterstitialAds(Activity activity, MyCallback myCallback) {
        if (Global.inter_back && InterstitialAdManager.canShowAd() && Global.isInternetConnected(activity)) {
            Admob.getInstance().showDialog(activity);
            if (mBackInterstitialAd == null) {
                ShowBAckInterstitial(activity, myCallback);
            } else {
                ERainAd.getInstance().forceShowInterstitial(activity, mBackInterstitialAd, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        myCallback.callbackCall();
                        InterstitialAdManager.onAdShown();
                    }
                }, true);
            }
        } else {
            myCallback.callbackCall();
        }
    }

    private static void ShowBAckInterstitial(Activity activity, MyCallback myCallback) {
        ERainAd.getInstance().getInterstitialAds(activity, getInterBack(), new AdCallback() {
            @Override
            public void onApInterstitialLoad(@Nullable ApInterstitialAd apInterstitialAd) {
                super.onApInterstitialLoad(apInterstitialAd);
                mBackInterstitialAd = apInterstitialAd;
                ERainAd.getInstance().forceShowInterstitial(activity, mBackInterstitialAd, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        myCallback.callbackCall();
                        InterstitialAdManager.onAdShown();
                    }
                }, true);
            }
        });
    }
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

}
