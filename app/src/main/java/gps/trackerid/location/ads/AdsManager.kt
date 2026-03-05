package gps.trackerid.location.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ads.module.ads.ERainAd
import com.ads.module.ads.wrapper.ApInterstitialAd
import com.ads.module.ads.wrapper.ApNativeAd
import com.ads.module.funtion.AdCallback
import com.ads.module.util.AppConstant
import com.google.android.gms.ads.LoadAdError
import gps.trackerid.location.BuildConfig
import gps.trackerid.location.R
import gps.trackerid.location.databinding.ShimmerNativeMediumBinding
import gps.trackerid.location.databinding.ShimmerNativeMiddleBinding

fun Context.isNetwork(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected == true
}

@SuppressLint("StaticFieldLeak")
object AdsManager {
    enum class NativeLanguageType {
        NORMAL,
        CLICK,
    }

    var nativeAdLanguageNormal: ApNativeAd? = null
    var nativeAdLanguageClick: ApNativeAd? = null
    var nativeAdObFull: ApNativeAd? = null
    var mInterOb: ApInterstitialAd? = null
    var mInterHome: ApInterstitialAd? = null
    var mInterBack: ApInterstitialAd? = null

    fun resetAds() {
        nativeAdLanguageNormal = null
        nativeAdLanguageClick = null
        nativeAdObFull = null
        mInterOb = null
        mInterHome = null
        mInterBack = null
    }

    var preLoadNativeListener: PreLoadNativeListener? = null

    fun setPreLoadNativeCallback(listener: PreLoadNativeListener) {
        preLoadNativeListener = listener
    }

    fun loadBannerSplash(activity: Activity, frAds: FrameLayout) {
        val layout = LayoutInflater.from(activity)
            .inflate(com.ads.module.R.layout.layout_banner_control, null)
        frAds.removeAllViews()
        frAds.addView(layout)
        if (RemoteUtils.getOnBannerSplash() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadBannerSplash: 1")
            ERainAd.getInstance()
                .loadBanner(activity, BuildConfig.banner_splash, object : AdCallback() {
                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        frAds.removeAllViews()
                    }
                })
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadNativeLanguageNormal(activity: Activity, isOpen: Boolean) {
        if (nativeAdLanguageNormal == null) {
            if (RemoteUtils.getOnNativeLanguage1() && activity.isNetwork() && isOpen.not()) {
                ERainAd.getInstance().loadNativeAdResultCallback(
                    activity,
                    BuildConfig.native_language_1,
                    R.layout.layout_native_ad_medium,
                    object : AdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            Log.d("DEV_ITG", "loadNativeLanguageNormal: native_language_1")
                            nativeAdLanguageNormal = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            }

            if (RemoteUtils.getOnNativeLanguage2Click() && activity.isNetwork() && isOpen) {
                ERainAd.getInstance().loadNativeAdResultCallback(
                    activity,
                    BuildConfig.native_language_2,
                    R.layout.layout_native_ad_medium,
                    object : AdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            Log.d("DEV_ITG", "loadNativeLanguageNormal: native_language_2")
                            nativeAdLanguageNormal = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            }

        }
    }

    fun loadNativeLanguageClick(activity: Activity, isOpen: Boolean) {
        if (nativeAdLanguageClick == null) {
            if (RemoteUtils.getOnNativeLanguage1Click() && activity.isNetwork() && isOpen.not()) {
                ERainAd.getInstance().loadNativeAdResultCallback(
                    activity,
                    BuildConfig.native_language_1_click,
                    R.layout.layout_native_ad_medium,
                    object : AdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            Log.d("DEV_ITG", "loadNativeLanguageClick: native_language_1_click")
                            nativeAdLanguageClick = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            }

            if (RemoteUtils.getOnNativeLanguage2Click() && activity.isNetwork() && isOpen) {
                ERainAd.getInstance().loadNativeAdResultCallback(
                    activity,
                    BuildConfig.native_language_2_click,
                    R.layout.layout_native_ad_medium,
                    object : AdCallback() {
                        override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                            super.onNativeAdLoaded(nativeAd)
                            Log.d("DEV_ITG", "loadNativeLanguageClick: native_language_2_click")
                            nativeAdLanguageClick = nativeAd
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeSuccess()
                            }
                        }

                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            if (preLoadNativeListener != null) {
                                preLoadNativeListener?.onLoadNativeFail()
                            }
                        }
                    })
            }

        }
    }

    fun loadNativeOb1(activity: Activity, isOpen: Boolean, frAds: FrameLayout) {
        val layoutShimmer = ShimmerNativeMediumBinding.inflate(activity.layoutInflater)
        frAds.removeAllViews()
        frAds.addView(layoutShimmer.shimmerNative)
        if (activity.isNetwork()) {
            if (isOpen.not()) {
                if (RemoteUtils.getOnNativeOnboarding11()) {
                    Log.d("DEV_ITG", "loadNativeOb4: 11")
                    ERainAd.getInstance().loadNativeAdResultCallback(
                        activity,
                        BuildConfig.native_onboarding_1_1,
                        R.layout.layout_native_ad_medium,
                        object : AdCallback() {
                            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                                super.onNativeAdLoaded(nativeAd)
                                populateNativeAdView(
                                    activity, nativeAd, frAds, layoutShimmer.shimmerNative,
                                    RemoteUtils.getCTAButtonHeight().toInt()
                                )
                            }

                            override fun onAdFailedToLoad(i: LoadAdError?) {
                                super.onAdFailedToLoad(i)
                                frAds.removeAllViews()
                            }
                        }
                    )
                } else {
                    frAds.removeAllViews()
                }
            } else {
                if (RemoteUtils.getOnNativeOnboarding21()) {
                    Log.d("DEV_ITG", "loadNativeOb4: 21")
                    ERainAd.getInstance().loadNativeAdResultCallback(
                        activity,
                        BuildConfig.native_onboarding_2_1,
                        R.layout.layout_native_ad_medium,
                        object : AdCallback() {
                            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                                super.onNativeAdLoaded(nativeAd)
                                populateNativeAdView(
                                    activity, nativeAd, frAds, layoutShimmer.shimmerNative,
                                    RemoteUtils.getCTAButtonHeight().toInt()
                                )
                            }

                            override fun onAdFailedToLoad(i: LoadAdError?) {
                                super.onAdFailedToLoad(i)
                                frAds.removeAllViews()
                            }
                        }
                    )
                } else {
                    frAds.removeAllViews()
                }
            }
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadNativeOb4(activity: Activity, isOpen: Boolean, frAds: FrameLayout) {
        val layoutShimmer = ShimmerNativeMediumBinding.inflate(activity.layoutInflater)
        frAds.removeAllViews()
        frAds.addView(layoutShimmer.shimmerNative)
        if (activity.isNetwork()) {
            if (isOpen.not()) {
                if (RemoteUtils.getOnNativeOnboarding11()) {
                    Log.d("DEV_ITG", "loadNativeOb4: 14")
                    ERainAd.getInstance().loadNativeAd(
                        activity,
                        BuildConfig.native_onboarding_1_4,
                        R.layout.layout_native_ad_medium,
                        frAds,
                        layoutShimmer.shimmerNative, object : AdCallback() {
                            override fun onAdFailedToLoad(i: LoadAdError?) {
                                super.onAdFailedToLoad(i)
                                frAds.removeAllViews()
                            }
                        }
                    )
                } else {
                    frAds.removeAllViews()
                }
            } else {
                if (RemoteUtils.getOnNativeOnboarding24()) {
                    Log.d("DEV_ITG", "loadNativeOb4: 24")
                    ERainAd.getInstance().loadNativeAd(
                        activity,
                        BuildConfig.native_onboarding_2_4,
                        R.layout.layout_native_ad_medium,
                        frAds,
                        layoutShimmer.shimmerNative, object : AdCallback() {
                            override fun onAdFailedToLoad(i: LoadAdError?) {
                                super.onAdFailedToLoad(i)
                                frAds.removeAllViews()
                            }
                        }
                    )
                } else {
                    frAds.removeAllViews()
                }
            }
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadNativeObFull(activity: Activity, isOpen: Boolean) {
        if (activity.isNetwork()) {
            if (isOpen.not()) {
                if (RemoteUtils.getOnNativeOnboardingFullscreen12() && ERainAd.getInstance().shouldDisplayNativeOnboardingFull1) {
                    ERainAd.getInstance().loadNativeAdResultCallback(
                        activity,
                        BuildConfig.native_onboarding_fullscreen_1_2,
                        R.layout.layout_native_ad_full,
                        object : AdCallback() {
                            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                                super.onNativeAdLoaded(nativeAd)
                                nativeAdObFull = nativeAd
                                Log.d("DEV_ITG", "onNativeAdLoaded: Full 1")
                            }
                        }
                    )
                }
            } else {
                if (RemoteUtils.getOnNativeOnboarding21() && ERainAd.getInstance().shouldDisplayNativeOnboardingFull1) {
                    ERainAd.getInstance().loadNativeAdResultCallback(
                        activity,
                        BuildConfig.native_onboarding_fullscreen_2_2,
                        R.layout.layout_native_ad_full,
                        object : AdCallback() {
                            override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                                super.onNativeAdLoaded(nativeAd)
                                nativeAdObFull = nativeAd
                                Log.d("DEV_ITG", "onNativeAdLoaded: Full 2")
                            }
                        }
                    )
                }
            }
        }
    }

    fun loadInterOb(context: Context) {
        if (mInterOb == null && RemoteUtils.getOnInterOnboarding() && context.isNetwork() && ERainAd.getInstance().shouldDisplayInterOnboarding) {
            mInterOb = ERainAd.getInstance()
                .getInterstitialAds(context, BuildConfig.inter_onboarding, object :
                    AdCallback() {})
        }
    }

    fun showInterOb(context: Context, onNextAction: () -> Unit) {
        if (RemoteUtils.getOnInterOnboarding() && ERainAd.getInstance().shouldDisplayInterOnboarding && context.isNetwork()) {
            ERainAd.getInstance()
                .forceShowInterstitial(context, mInterOb, object : AdCallback() {
                    override fun onNextAction() {
                        super.onNextAction()
                        onNextAction()
                    }
                }, false)
        } else {
            onNextAction()
        }
    }

    fun loadNativeUninstall(activity: Activity, frAds: FrameLayout) {
        val layoutShimmer = ShimmerNativeMediumBinding.inflate(activity.layoutInflater)
        frAds.removeAllViews()
        frAds.addView(layoutShimmer.shimmerNative)
        if (RemoteUtils.getOnNativeUninstall() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadNativeUninstall: ")
            ERainAd.getInstance().loadNativeAdResultCallback(
                activity,
                BuildConfig.native_uninstall,
                R.layout.layout_native_ad_medium, object : AdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        populateNativeAdView(
                            activity, nativeAd, frAds, layoutShimmer.shimmerNative,
                            RemoteUtils.getCTAButtonHeight().toInt()
                        )
                    }

                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        frAds.removeAllViews()
                    }
                }
            )
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadNativeSurveyUninstall(activity: Activity, frAds: FrameLayout) {
        val layoutShimmer = ShimmerNativeMediumBinding.inflate(activity.layoutInflater)
        frAds.removeAllViews()
        frAds.addView(layoutShimmer.shimmerNative)
        if (RemoteUtils.getOnNativeSurveyUninstall() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadNativeSurveyUninstall: ")
            ERainAd.getInstance().loadNativeAdResultCallback(
                activity,
                BuildConfig.native_survey_uninstall,
                R.layout.layout_native_ad_medium,
                object : AdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        populateNativeAdView(
                            activity, nativeAd, frAds, layoutShimmer.shimmerNative,
                            RemoteUtils.getCTAButtonHeight().toInt()
                        )
                    }

                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        frAds.removeAllViews()
                    }
                }
            )
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadBannerHome(activity: Activity, frAds: FrameLayout) {
        val layout = LayoutInflater.from(activity)
            .inflate(com.ads.module.R.layout.layout_banner_control, null)
        frAds.removeAllViews()
        frAds.addView(layout)
        if (RemoteUtils.getOnBannerCollapHome() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadBannerHome: 1")
            ERainAd.getInstance()
                .loadCollapsibleBanner(
                    activity,
                    BuildConfig.banner_collap_home,
                    AppConstant.CollapsibleGravity.BOTTOM,
                    object : AdCallback() {
                        override fun onAdFailedToLoad(i: LoadAdError?) {
                            super.onAdFailedToLoad(i)
                            frAds.removeAllViews()
                        }
                    })
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadNativeHome(activity: Activity, frAds: FrameLayout) {
        val layoutShimmer = ShimmerNativeMiddleBinding.inflate(activity.layoutInflater)
        frAds.removeAllViews()
        frAds.addView(layoutShimmer.shimmerNative)
        if (RemoteUtils.getOnNativeHome() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadNativeHome: ")
            ERainAd.getInstance().loadNativeAdResultCallback(
                activity,
                BuildConfig.native_home,
                R.layout.layout_native_ad_middle,
                object : AdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        populateNativeAdView(
                            activity, nativeAd, frAds, layoutShimmer.shimmerNative,
                            RemoteUtils.getCTAButtonHeight().toInt()
                        )
                    }

                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        frAds.removeAllViews()
                    }
                }
            )
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadNativeSetting(activity: Activity, frAds: FrameLayout) {
        val layoutShimmer = ShimmerNativeMediumBinding.inflate(activity.layoutInflater)
        frAds.removeAllViews()
        frAds.addView(layoutShimmer.shimmerNative)
        if (RemoteUtils.getOnNativeSetting() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadNativeSetting: ")
            ERainAd.getInstance().loadNativeAdResultCallback(
                activity,
                BuildConfig.native_setting,
                R.layout.layout_native_ad_medium,
                object : AdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        populateNativeAdView(
                            activity, nativeAd, frAds, layoutShimmer.shimmerNative,
                            RemoteUtils.getCTAButtonHeight().toInt()
                        )
                    }

                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        frAds.removeAllViews()
                    }
                }
            )
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadNativePhoneLocator(activity: Activity, frAds: FrameLayout) {
        val layoutShimmer = ShimmerNativeMiddleBinding.inflate(activity.layoutInflater)
        frAds.removeAllViews()
        frAds.addView(layoutShimmer.shimmerNative)
        if (RemoteUtils.getOnNativePhoneLocator() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadNativePhoneLocator: ")
            ERainAd.getInstance().loadNativeAdResultCallback(
                activity,
                BuildConfig.native_phone_locator,
                R.layout.layout_native_ad_middle,
                object : AdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        super.onNativeAdLoaded(nativeAd)
                        populateNativeAdView(
                            activity, nativeAd, frAds, layoutShimmer.shimmerNative,
                            RemoteUtils.getCTAButtonHeight().toInt()
                        )
                    }

                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        frAds.removeAllViews()
                    }
                }
            )
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadBannerAll(activity: Activity, frAds: FrameLayout) {
        val layout = LayoutInflater.from(activity)
            .inflate(com.ads.module.R.layout.layout_banner_control, null)
        frAds.removeAllViews()
        frAds.addView(layout)
        if (RemoteUtils.getOnBannerSplash() && activity.isNetwork()) {
            Log.d("DEV_ITG", "loadBannerSplash: 1")
            ERainAd.getInstance()
                .loadBanner(activity, BuildConfig.banner_all, object : AdCallback() {
                    override fun onAdFailedToLoad(i: LoadAdError?) {
                        super.onAdFailedToLoad(i)
                        frAds.removeAllViews()
                    }
                })
        } else {
            frAds.removeAllViews()
        }
    }

    fun loadInterHome(context: Context) {
        if (mInterHome == null && RemoteUtils.getOnInterHome() && context.isNetwork()) {
            mInterHome = ERainAd.getInstance()
                .getInterstitialAds(context, BuildConfig.inter_home, object :
                    AdCallback() {})
        }
    }

    fun loadInterBack(context: Context) {
        if (mInterBack == null && RemoteUtils.getOnInterBack() && context.isNetwork()) {
            mInterBack = ERainAd.getInstance()
                .getInterstitialAds(context, BuildConfig.inter_back, object :
                    AdCallback() {})
        }
    }

    fun showInterHome(context: Context, onNextAction: () -> Unit) {
        if (RemoteUtils.getOnInterHome() && context.isNetwork()) {
            ERainAd.getInstance().forceShowInterstitial(context, mInterHome, object : AdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    onNextAction()
                }
            }, true)
        } else {
            onNextAction()
        }
    }

    fun showInterBack(context: Context, onNextAction: () -> Unit) {
        if (RemoteUtils.getOnInterBack() && context.isNetwork()) {
            ERainAd.getInstance().forceShowInterstitial(context, mInterBack, object : AdCallback() {
                override fun onNextAction() {
                    super.onNextAction()
                    onNextAction()
                }
            }, true)
        } else {
            onNextAction()
        }
    }
}