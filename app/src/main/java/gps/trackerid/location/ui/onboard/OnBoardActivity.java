package gps.trackerid.location.ui.onboard;

import static gps.trackerid.location.ads.AdsManagerKt.isNetwork;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ads.module.ads.ERainAd;

import java.util.ArrayList;
import java.util.List;

import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.ShortcutUtils;
import gps.trackerid.location.databinding.ActivityOnboardBinding;
import gps.trackerid.location.ui.PermissionActivity;
import gps.trackerid.location.ui.TimestampActivity;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.ui.baseui.BaseFragment;

public class OnBoardActivity extends BaseActivity {
    ActivityOnboardBinding onboardBinding;
    public static OnBoardActivity onBoardActivity;

    public static OnBoardActivity getInstance() {
        return onBoardActivity;
    }


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onboardBinding = ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(onboardBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        onBoardActivity = this;

        ShortcutUtils.INSTANCE.checkInit(this);

        initViewPager();
    }

    private void initViewPager() {
        boolean isAddObFull = (ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1() || ERainAd.getInstance().getShouldDisplayNativeOnboardingFull1());
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new OnBoardFragment1());
        fragments.add(new OnBoardFragment2());
        if (isAddObFull && isNetwork(this)) {
            fragments.add(new OnBoardFullFragment());
        }
        fragments.add(new OnBoardFragment3());
        fragments.add(new OnBoardFragment4());

        OnBoardAdapter adapter = new OnBoardAdapter(this, fragments);
        onboardBinding.viewPager.setOffscreenPageLimit(5);
        onboardBinding.viewPager.setAdapter(adapter);

        AdsManager.INSTANCE.loadInterOb(this);
    }

    public void nextPage() {
        onboardBinding.viewPager.setCurrentItem(onboardBinding.viewPager.getCurrentItem() + 1);
    }

    public void launchHomeScreen() {
        if (IsCheckPermission()) {
            startActivity(new Intent(OnBoardActivity.this, TimestampActivity.class));
            finish();
        } else {
            startActivity(new Intent(OnBoardActivity.this, PermissionActivity.class));
            finish();
        }

    }

    public ArrayList<String> mPermissions;

    private boolean IsTaken(List<String> list, String str) {
        if (ContextCompat.checkSelfPermission(this, str) != 0) {
            list.add(str);
            return ActivityCompat.shouldShowRequestPermissionRationale(this, str);
        }
        return true;
    }

    private boolean IsCheckPermission() {
        mPermissions = new ArrayList();
        if (!IsTaken(mPermissions, "android.permission.ACCESS_COARSE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_COARSE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.ACCESS_FINE_LOCATION")) {
            mPermissions.add("android.permission.ACCESS_FINE_LOCATION");
        }
        if (!IsTaken(mPermissions, "android.permission.CAMERA")) {
            mPermissions.add("android.permission.CAMERA");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!IsTaken(mPermissions, "android.permission.POST_NOTIFICATIONS")) {
                mPermissions.add("android.permission.POST_NOTIFICATIONS");
            }
        }

        return mPermissions.isEmpty();
    }

}