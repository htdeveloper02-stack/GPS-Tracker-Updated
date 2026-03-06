package gps.trackerid.location;

import com.ads.module.admob.Admob;
import com.ads.module.admob.AppOpenManager;
import com.ads.module.ads.ERainAd;
import com.ads.module.application.AdsMultiDexApplication;
import com.ads.module.config.AdjustConfig;
import com.ads.module.config.ERainAdConfig;

import java.util.ArrayList;

import gps.trackerid.location.ads.SharedUtils;
import gps.trackerid.location.ui.LangActivity;
import gps.trackerid.location.ui.PermissionActivity;
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
        mERainAdConfig.setIdAdResume(BuildConfig.open_resume);
        mERainAdConfig.setIntervalInterstitialAd(30);
        ArrayList<String> listDeice = new ArrayList<>();
        listDeice.add("06B2A9129A4C94D76A0B92B5AC4314FD");
        mERainAdConfig.setListDeviceTest(listDeice);

        ERainAd.getInstance().init(this, mERainAdConfig);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        AppOpenManager.getInstance().disableAppResumeWithActivity(LangActivity.class);
        AppOpenManager.getInstance().disableAppResumeWithActivity(OnBoardActivity.class);
        AppOpenManager.getInstance().disableAppResumeWithActivity(PermissionActivity.class);
    }
}
