package gps.trackerid.location.ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.ads.module.ads.ERainAd
import com.ads.module.ads.wrapper.ApNativeAd
import com.ads.module.funtion.AdCallback
import com.google.android.gms.ads.LoadAdError
import gps.trackerid.location.BuildConfig
import gps.trackerid.location.R
import gps.trackerid.location.databinding.ShimmerNativeMediumBinding

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

    fun resetAds() {
        nativeAdLanguageNormal = null
        nativeAdLanguageClick = null
        nativeAdObFull = null
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
                if (RemoteUtils.getOnNativeOnboarding21()) {
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
                            }
                        }
                    )
                }
            }
        }
    }
}