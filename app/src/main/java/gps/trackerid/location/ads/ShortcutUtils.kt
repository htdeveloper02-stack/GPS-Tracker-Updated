package gps.trackerid.location.ads

import android.app.Activity
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.ads.module.ads.ERainAd
import gps.trackerid.location.R
import gps.trackerid.location.ui.NearByActivity
import gps.trackerid.location.ui.PhoneLocator
import gps.trackerid.location.ui.SplashActivity
import gps.trackerid.location.ui.TrafficAlertActivity
import gps.trackerid.location.utils.Global

object ShortcutUtils {
    fun checkInit(activity: Activity) {
        if (ERainAd.getInstance().shouldDisplayWidgetUninstall) {
            initShortCut(activity)
        }
    }
    private fun initShortCut(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
        try {
            val shortcutManager: ShortcutManager =
                activity.getSystemService(ShortcutManager::class.java) ?: return
            shortcutManager.removeAllDynamicShortcuts()
            val shortcuts = ArrayList<ShortcutInfo?>()
            shortcuts.add(
                ShortcutInfo.Builder(activity, "shortcut_locator")
                    .setShortLabel(activity.getString(R.string.phonelocator))
                    .setIcon(
                        Icon.createWithResource(
                            activity, R.drawable.ic_shortcut_locator
                        )
                    )
                    .setIntent(
                        createShortcutIntent(
                            activity,
                            SplashActivity::class.java,
                            "android.intent.action.SHORTCUT_PHONE_LOCATOR",
                            Global.ACTION_OPEN_LOCATOR
                        )
                    )
                    .setRank(0)
                    .build()
            )
            shortcuts.add(
                ShortcutInfo.Builder(activity, "hortcut_traffic")
                    .setShortLabel(activity.getString(R.string.traffic))
                    .setIcon(
                        Icon.createWithResource(
                            activity, R.drawable.ic_shortcut_traffic
                        )
                    )
                    .setIntent(
                        createShortcutIntent(
                            activity,
                            SplashActivity::class.java,
                            "android.intent.action.SHORTCUT_TRAFFIC_ALERT",
                            Global.ACTION_OPEN_TRAFFIC_ALERT
                        )
                    )
                    .setRank(1)
                    .build()
            )
            shortcuts.add(
                ShortcutInfo.Builder(activity, "shortcut_nearby")
                    .setShortLabel(activity.getString(R.string.nearby))
                    .setIcon(
                        Icon.createWithResource(
                            activity, R.drawable.ic_shortcut_nearby
                        )
                    )
                    .setIntent(
                        createShortcutIntent(
                            activity,
                            SplashActivity::class.java,
                            "android.intent.action.SHORTCUT_NEAR_BY",
                            Global.ACTION_OPEN_NEAR_BY
                        )
                    )
                    .setRank(2)
                    .build()
            )

            shortcuts.add(
                ShortcutInfo.Builder(activity, "shortcut_uninstall")
                    .setShortLabel(activity.getString(R.string.txt_uninstall))
                    .setIcon(
                        Icon.createWithResource(
                            activity, R.drawable.ic_shortcut_uninstall
                        )
                    )
                    .setIntent(
                        createShortcutIntent(
                            activity,
                            SplashActivity::class.java,
                            "android.intent.action.SHORTCUT_UNINSTALL_APP",
                            Global.ACTION_OPEN_UNINSTALL
                        )
                    )
                    .setRank(3)
                    .build()
            )

            shortcutManager.dynamicShortcuts = shortcuts
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createShortcutIntent(
        activity: Activity,
        target: Class<*>?,
        action: String?,
        shortcutType: String?
    ): Intent {
        val intent = Intent(activity, target)
        intent.action = action
        intent.putExtra(Global.FROM_SHORTCUT, shortcutType)

        // Clears task properly
        intent.addFlags(
            (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        )

        return intent
    }
}