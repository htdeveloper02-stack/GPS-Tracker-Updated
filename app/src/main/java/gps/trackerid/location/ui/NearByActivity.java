package gps.trackerid.location.ui;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ads.module.ads.ERainAd;

import java.util.Arrays;
import java.util.List;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.NearByAdapter;
<<<<<<< HEAD
import gps.trackerid.location.adshelper.InterstitialAdManager;
=======
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.databinding.ActivityNearbyBinding;
import gps.trackerid.location.models.NearbyItem;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class NearByActivity extends BaseActivity {

    ActivityNearbyBinding binding;
    private List<NearbyItem> nearbyList;
    NearByAdapter nearByAdapter;
    private boolean isShortcut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNearbyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        handleShortcutIntent(getIntent());

        mSetData();

        if (Global.banner_all && Global.isInternetConnected(NearByActivity.this)) {
            binding.mRlBanner.setVisibility(VISIBLE);
            ERainAd.getInstance().loadBanner(this, getBannerAll());
        }
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });
    }

    private void handleShortcutIntent(Intent intent) {
        if (intent != null && "android.intent.action.SHORTCUT_NEAR_BY".equals(intent.getAction())) {
            // Open the VPN Server screen
            isShortcut = true;
            setRemoteConfigListener(new RemoteConfigListener() {
                @Override
                public void onRemoteConfigLoaded() {
                    if (Global.banner_all && Global.isInternetConnected(NearByActivity.this)) {
                        binding.mRlBanner.setVisibility(VISIBLE);
                        ERainAd.getInstance().loadBanner(NearByActivity.this, getBannerAll());
                    }
                }
            });
        } else {
            isShortcut = false;
        }

    }

    private void onBackCall() {
<<<<<<< HEAD
        InterstitialAdManager.showIfReady(
                NearByActivity.this,
                "inter_back",
                () -> {
                    if (isShortcut) {
                        startActivity(new Intent(NearByActivity.this, TimestampActivity.class));
                        finish();
                    } else {
                        finish();
                    }
                }
        );
=======
        loadBAckInterstitialAds(NearByActivity.this, new AdsConfig.MyCallback() {
            @Override
            public void callbackCall() {
                if (isShortcut) {
                    startActivity(new Intent(NearByActivity.this, TimestampActivity.class));
                    finish();
                } else {
                    finish();
                }
            }
        });
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
    }

    private void mSetData() {
        nearbyList = Arrays.asList(
                new NearbyItem(R.drawable.ic_art_gallery,
                        getResources().getString(R.string.art_gallery),
                        "https://www.google.com/maps/search/Art+Gallery"),

                new NearbyItem(R.drawable.ic_nea_by_bar,
                        getResources().getString(R.string.bar),
                        "https://www.google.com/maps/search/Bars"),

                new NearbyItem(R.drawable.ic_near_by_bank,
                        getResources().getString(R.string.bank),
                        "https://www.google.com/maps/search/Bank"),

                new NearbyItem(R.drawable.ic_nea_by_atm,
                        getResources().getString(R.string.atm),
                        "https://www.google.com/maps/search/ATM"),

                new NearbyItem(R.drawable.ic_nea_by_saloon,
                        getResources().getString(R.string.saloon),
                        "https://www.google.com/maps/search/Beauty+Salon"),

                new NearbyItem(R.drawable.ic_nea_by_book_store,
                        getResources().getString(R.string.book),
                        "https://www.google.com/maps/search/Book+Store"),

                new NearbyItem(R.drawable.ic_nea_by_coffee,
                        getResources().getString(R.string.coffee),
                        "https://www.google.com/maps/search/Coffee+Shop"),

                new NearbyItem(R.drawable.ic_nea_by_car_dealer,
                        getResources().getString(R.string.cardealer),
                        "https://www.google.com/maps/search/Car+Dealer"),

                new NearbyItem(R.drawable.ic_nea_by_gas,
                        getResources().getString(R.string.bygas),
                        "https://www.google.com/maps/search/Gas+Station"),

                new NearbyItem(R.drawable.ic_nea_by_gym,
                        getResources().getString(R.string.bygym),
                        "https://www.google.com/maps/search/Gym"),

                new NearbyItem(R.drawable.ic_nea_by_hospital,
                        getResources().getString(R.string.hospital),
                        "https://www.google.com/maps/search/Hospital"),

                new NearbyItem(R.drawable.ic_nea_by_hotel,
                        getResources().getString(R.string.byhotel),
                        "https://www.google.com/maps/search/Hotels"),

                new NearbyItem(R.drawable.ic_nea_by_parking,
                        getResources().getString(R.string.parking),
                        "https://www.google.com/maps/search/Parking"),

                new NearbyItem(R.drawable.ic_nea_by_restuarent,
                        getResources().getString(R.string.retuarant),
                        "https://www.google.com/maps/search/Restaurant")
        );
        nearByAdapter = new NearByAdapter(NearByActivity.this, nearbyList, new NearByAdapter.OnClickByNearBy() {
            @Override
            public void OnClickBy(NearbyItem nearbyItem) {
                mOpenInMaps(nearbyItem.getMapUrl());
            }
        });
        binding.mRvNearBy.setAdapter(nearByAdapter);
        binding.mRvNearBy.setLayoutManager(new GridLayoutManager(this, 1));
        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
    }

    private void mOpenInMaps(String mapUrl) {
        try {
            try {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(mapUrl));
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(268435456);
                startActivity(intent);
            } catch (Exception unused) {
                Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(mapUrl));
                intent2.setFlags(268435456);
                startActivity(intent2);
            }
        } catch (Exception unused2) {
            Toast.makeText(this, getResources().getString(R.string.unable_map), 0).show();
        }
    }
}