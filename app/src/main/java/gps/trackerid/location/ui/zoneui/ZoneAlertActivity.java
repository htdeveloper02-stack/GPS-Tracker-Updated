package gps.trackerid.location.ui.zoneui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ads.module.ads.ERainAd;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.ZoneAdapter;
<<<<<<< HEAD
import gps.trackerid.location.adshelper.InterstitialAdManager;
=======
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.databinding.ActivityZonealertBinding;
import gps.trackerid.location.models.zonedata.AppDatabase;
import gps.trackerid.location.models.zonedata.DataZone;
import gps.trackerid.location.models.zonedata.DataZoneDao;
import gps.trackerid.location.models.zonedata.DataZoneViewModel;
import gps.trackerid.location.models.zonedata.DataZoneViewModelFactory;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class ZoneAlertActivity extends BaseActivity {
    ActivityZonealertBinding zonealertBinding;
    private DataZoneViewModel viewModel;
    ZoneAdapter zoneAdapter;
    boolean ISDataExist = false;
    private ActivityResultLauncher<Intent> createZoneLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            DataZone dataZone;

                            if (android.os.Build.VERSION.SDK_INT >= 33) {
                                dataZone = result.getData()
                                        .getSerializableExtra("selectedZone", DataZone.class);
                            } else {
                                dataZone = (DataZone) result.getData()
                                        .getSerializableExtra("selectedZone");
                            }

                            if (dataZone != null) {
                                if (ISDataExist) {
                                    viewModel.update(dataZone);
                                } else {
                                    viewModel.insert(dataZone);
                                }
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zonealertBinding = ActivityZonealertBinding.inflate(getLayoutInflater());
        setContentView(zonealertBinding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DataZoneDao dao = AppDatabase.getInstance(this).dataZoneDao();

        viewModel = new ViewModelProvider(
                this,
                new DataZoneViewModelFactory(dao)
        ).get(DataZoneViewModel.class);
        zonealertBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        zonealertBinding.mIvCreateZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ISDataExist = false;
                Intent intent = new Intent(ZoneAlertActivity.this, CreateZoneActivity.class);
                createZoneLauncher.launch(intent);
            }
        });
        zoneAdapter = new ZoneAdapter(ZoneAlertActivity.this, new ZoneAdapter.OnZoneClick() {
            @Override
            public void OnZonekBy(DataZone dataZone) {
                ISDataExist = true;
                Intent intent = new Intent(ZoneAlertActivity.this, CreateZoneActivity.class);
                intent.putExtra("selectedZone", dataZone);
                createZoneLauncher.launch(intent);
            }
        }, new ZoneAdapter.OnDeleteClick() {
            @Override
            public void OnDeleteBy(DataZone dataZone) {
                mshowDailog(dataZone);
            }
        });
        zonealertBinding.mRvZone.setAdapter(zoneAdapter);
        zonealertBinding.mRvZone.setLayoutManager(new GridLayoutManager(this, 1));

        viewModel.getAllZones().observe(this, zones -> {
            // update adapter here
            if (zoneAdapter != null && !zones.isEmpty()) {
                zonealertBinding.mTxtNoData.setVisibility(GONE);
                zonealertBinding.mRvZone.setVisibility(VISIBLE);
                zoneAdapter.setZone(zones);
            } else {
                zonealertBinding.mTxtNoData.setVisibility(VISIBLE);
                zonealertBinding.mRvZone.setVisibility(GONE);
            }
        });

        if (Global.banner_all && Global.isInternetConnected(ZoneAlertActivity.this)) {
            zonealertBinding.mRlBanner.setVisibility(VISIBLE);
            ERainAd.getInstance().loadBanner(this, getBannerAll());
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }

    private void onBackCall() {
<<<<<<< HEAD
        InterstitialAdManager.showIfReady(
                ZoneAlertActivity.this,
                "inter_back",
                () -> {
                    finish();
                }
        );

=======
        loadBAckInterstitialAds(ZoneAlertActivity.this, new AdsConfig.MyCallback() {
            @Override
            public void callbackCall() {
                finish();
            }
        });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
    }

    private void mshowDailog(DataZone dataZone) {
        String string = getResources().getString(R.string.delete_zone);
        String string2 = getResources().getString(R.string.mDeletSub);
        AlertDialog.Builder builder = new AlertDialog.Builder(ZoneAlertActivity.this, R.style.AppAlertDialogStyle);

        // below line is the title for our alert dialog.
        builder.setTitle(string);

        // below line is our message for our dialog
        builder.setMessage(string2);
        builder.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel();
            viewModel.delete(dataZone);

        });
        builder.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> {
            // this method is called when user click on negative button.
            dialog.cancel();
        });
        // below line is used to display our dialog
        builder.show();
    }
}