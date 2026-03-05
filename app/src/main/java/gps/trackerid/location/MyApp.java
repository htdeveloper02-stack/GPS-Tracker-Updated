package gps.trackerid.location;

import static gps.trackerid.location.adshelper.AdsConfig.getAppOpenResume;

import com.ads.module.admob.Admob;
import com.ads.module.admob.AppOpenManager;
import com.ads.module.ads.ERainAd;
import com.ads.module.application.AdsMultiDexApplication;
import com.ads.module.config.AdjustConfig;
import com.ads.module.config.ERainAdConfig;

import gps.trackerid.location.ads.SharedUtils;
import gps.trackerid.location.ui.LangActivity;
import gps.trackerid.location.ui.SplashActivity;
import gps.trackerid.location.ui.onboard.OnBoardActivity;

public class MyApp extends AdsMultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedUtils.INSTANCE.init(this);
        initAds();
    }

    private void initAds() {
        String environment = BuildConfig.DEBUG ? ERainAdConfig.ENVIRONMENT_DEVELOP : ERainAdConfig.ENVIRONMENT_PRODUCTION;
        mERainAdConfig = new ERainAdConfig(this, environment);

        AdjustConfig adjustConfig = new AdjustConfig(true, getString(R.string.adjust_token));
        mERainAdConfig.setAdjustConfig(adjustConfig);
        mERainAdConfig.setFacebookClientToken(getString(R.string.facebook_client_token));
        mERainAdConfig.setAdjustTokenTiktok(getString(R.string.tiktok_token));
        mERainAdConfig.setIdAdResume(getAppOpenResume());

        ERainAd.getInstance().init(this, mERainAdConfig);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        AppOpenManager.getInstance().disableAppResumeWithActivity(LangActivity.class);
        AppOpenManager.getInstance().disableAppResumeWithActivity(OnBoardActivity.class);
    }
}
