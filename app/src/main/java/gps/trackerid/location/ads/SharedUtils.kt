package gps.trackerid.location.ads

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedUtils {
    private const val PREFERENCES_NAME = "GPSTracker"
    private var sharePref: SharedPreferences? = null

    internal const val KEY_CONFIRM_CONSENT = "key_confirm_consent"
    internal const val KEY_IS_USER_GLOBAL = "key_is_user_global"
    internal const val OPEN_APP = "open_app"

    fun init(context: Context) {
        if (sharePref == null) {
            sharePref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }

    fun setValue(keyName: String, value: Any?) {
        sharePref?.edit {
            when (value) {
                is Int -> this.putInt(keyName, value)
                is Float -> this.putFloat(keyName, value)
                is Long -> this.putLong(keyName, value)
                is Boolean -> this.putBoolean(keyName, value)
                is String -> this.putString(keyName, value)
            }
        }
    }

    fun <T> getValue(keyName: String, defaultValue: T): T = when (defaultValue) {
        is Int -> (sharePref?.getInt(keyName, defaultValue) ?: defaultValue) as T
        is Long -> (sharePref?.getLong(keyName, defaultValue) ?: defaultValue) as T
        is Float -> (sharePref?.getFloat(keyName, defaultValue) ?: defaultValue) as T
        is Boolean -> (sharePref?.getBoolean(keyName, defaultValue) ?: defaultValue) as T
        is String -> (sharePref?.getString(keyName, defaultValue) ?: defaultValue) as T
        else -> defaultValue
    }
}