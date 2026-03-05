package gps.trackerid.location.ads

import android.annotation.SuppressLint
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import gps.trackerid.location.BuildConfig
import kotlin.math.max
import kotlin.math.min

@SuppressLint("StaticFieldLeak")
object RemoteUtils {
    private const val ON_INTER_SPLASH = "inter_splash"
    private const val ON_BANNER_SPLASH = "banner_splash"
    private const val ON_NATIVE_LANGUAGE_1 = "native_language_1"
    private const val ON_NATIVE_LANGUAGE_1_CLICK = "native_language_1_click"
    private const val ON_NATIVE_LANGUAGE_2 = "native_language_2"
    private const val ON_NATIVE_LANGUAGE_2_CLICK = "native_language_2_click"
    private const val ON_NATIVE_ONBOARDING_1_1 = "native_onboarding_1_1"
    private const val ON_NATIVE_ONBOARDING_2_1 = "native_onboarding_2_1"
    private const val ON_NATIVE_ONBOARDING_1_4 = "native_onboarding_1_4"
    private const val ON_NATIVE_ONBOARDING_2_4 = "native_onboarding_2_4"
    private const val ON_NATIVE_ONBOARDING_FULLSCREEN_1_2 = "native_onboarding_fullscreen_1_2"
    private const val ON_NATIVE_ONBOARDING_FULLSCREEN_2_2 = "native_onboarding_fullscreen_2_2"
    private const val ON_OPEN_RESUME = "open_resume"
    private const val ON_INTER_ONBOARDING = "inter_onboarding"
    private const val ON_INTER_HOME = "inter_home"
    private const val ON_NATIVE_HOME = "native_home"
    private const val ON_BANNER_COLLAP_HOME = "banner_collap_home"
    private const val ON_INTER_BACK = "inter_back"
    private const val ON_BANNER_ALL = "banner_all"
    private const val ON_NATIVE_PHONE_LOCATOR = "native_phone_locator"
    private const val ON_INTER_SPLASH_UNINSTALL = "inter_splash_uninstall"
    private const val ON_BANNER_SPLASH_UNINSTALL = "banner_splash_uninstall"
    private const val ON_NATIVE_UNINSTALL = "native_uninstall"
    private const val ON_NATIVE_SURVEY_UNINSTALL = "native_survey_uninstall"
    private const val ON_NATIVE_SETTING = "native_setting"

    private const val HEIGHT_BUTTON_CTA = "height_button_cta"
    private const val DELAY_BUTTON_DONE_LANGUAGE = "delay_button_done_language"

    private var completed = false
    private val DEFAULTS: HashMap<String, Any> =
        hashMapOf(
            ON_INTER_SPLASH to true,
            ON_BANNER_SPLASH to true,
            ON_NATIVE_LANGUAGE_1 to true,
            ON_NATIVE_LANGUAGE_1_CLICK to true,
            ON_NATIVE_LANGUAGE_2 to true,
            ON_NATIVE_LANGUAGE_2_CLICK to true,
            ON_NATIVE_ONBOARDING_1_1 to true,
            ON_NATIVE_ONBOARDING_2_1 to true,
            ON_NATIVE_ONBOARDING_1_4 to true,
            ON_NATIVE_ONBOARDING_2_4 to true,
            ON_NATIVE_ONBOARDING_FULLSCREEN_1_2 to true,
            ON_NATIVE_ONBOARDING_FULLSCREEN_2_2 to true,
            ON_OPEN_RESUME to true,
            ON_INTER_ONBOARDING to true,
            ON_INTER_HOME to true,
            ON_NATIVE_HOME to true,
            ON_BANNER_COLLAP_HOME to true,
            ON_INTER_BACK to true,
            ON_BANNER_ALL to true,
            ON_NATIVE_PHONE_LOCATOR to true,
            ON_INTER_SPLASH_UNINSTALL to true,
            ON_BANNER_SPLASH_UNINSTALL to true,
            ON_NATIVE_UNINSTALL to true,
            ON_NATIVE_SURVEY_UNINSTALL to true,
            ON_NATIVE_SETTING to true,

            DELAY_BUTTON_DONE_LANGUAGE to true,

            HEIGHT_BUTTON_CTA to 40
        )

    interface Listener {
        fun loadSuccess()
    }

    lateinit var listener: Listener
    private lateinit var remoteConfig: FirebaseRemoteConfig

    fun init(mListener: Listener) {
        listener = mListener
        remoteConfig =
            getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                0
            } else {
                60 * 60
            }
        }
        remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(DEFAULTS)
            fetchAndActivate().addOnCompleteListener {
                listener.loadSuccess()
                completed = true
            }
        }
        return remoteConfig
    }

    private fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return try {
            if (!completed) defaultValue else remoteConfig.getBoolean(key)
        } catch (ex: Exception) {
            ex.printStackTrace()
            defaultValue
        }
    }

    fun getOnInterSplash() = getBoolean(ON_INTER_SPLASH)
    fun getOnBannerSplash() = getBoolean(ON_BANNER_SPLASH)
    fun getOnNativeLanguage1() = getBoolean(ON_NATIVE_LANGUAGE_1)
    fun getOnNativeLanguage1Click() = getBoolean(ON_NATIVE_LANGUAGE_1_CLICK)
    fun getOnNativeLanguage2() = getBoolean(ON_NATIVE_LANGUAGE_2)
    fun getOnNativeLanguage2Click() = getBoolean(ON_NATIVE_LANGUAGE_2_CLICK)
    fun getOnNativeOnboarding11() = getBoolean(ON_NATIVE_ONBOARDING_1_1)
    fun getOnNativeOnboarding21() = getBoolean(ON_NATIVE_ONBOARDING_2_1)
    fun getOnNativeOnboarding14() = getBoolean(ON_NATIVE_ONBOARDING_1_4)
    fun getOnNativeOnboarding24() = getBoolean(ON_NATIVE_ONBOARDING_2_4)
    fun getOnNativeOnboardingFullscreen12() = getBoolean(ON_NATIVE_ONBOARDING_FULLSCREEN_1_2)
    fun getOnNativeOnboardingFullscreen22() = getBoolean(ON_NATIVE_ONBOARDING_FULLSCREEN_2_2)
    fun getOnOpenResume() = getBoolean(ON_OPEN_RESUME)
    fun getOnInterOnboarding() = getBoolean(ON_INTER_ONBOARDING)
    fun getOnInterHome() = getBoolean(ON_INTER_HOME)
    fun getOnNativeHome() = getBoolean(ON_NATIVE_HOME)
    fun getOnBannerCollapHome() = getBoolean(ON_BANNER_COLLAP_HOME)
    fun getOnInterBack() = getBoolean(ON_INTER_BACK)
    fun getOnBannerAll() = getBoolean(ON_BANNER_ALL)
    fun getOnNativePhoneLocator() = getBoolean(ON_NATIVE_PHONE_LOCATOR)
    fun getOnInterSplashUninstall() = getBoolean(ON_INTER_SPLASH_UNINSTALL)
    fun getOnBannerSplashUninstall() = getBoolean(ON_BANNER_SPLASH_UNINSTALL)
    fun getOnNativeUninstall() = getBoolean(ON_NATIVE_UNINSTALL)
    fun getOnNativeSurveyUninstall() = getBoolean(ON_NATIVE_SURVEY_UNINSTALL)
    fun getOnNativeSetting() = getBoolean(ON_NATIVE_SETTING)
    fun getDelayButtonDoneLanguage() = getBoolean(DELAY_BUTTON_DONE_LANGUAGE)

    fun getCTAButtonHeight(): Long {
        return try {
            if (!completed) {
                40
            } else {
                var value = remoteConfig.getLong(HEIGHT_BUTTON_CTA)
                value = min(max(value, 34), 52)
                value
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            40
        }
    }
}