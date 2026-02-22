package gps.trackerid.location.ui.phonetracker;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ads.module.ads.ERainAd;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.StopwatchAdapter;
<<<<<<< HEAD
=======
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.databinding.ActivityStopwatchBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.StopWatchUtils;
import gps.trackerid.location.utils.StopwatchHelper;

public class StopwatchAct extends BaseActivity {
    StopwatchAct stopwatchAct;
    ActivityStopwatchBinding binding;
    private StopwatchAdapter adapterStopwatch;
    public Handler handler;
    private StopwatchHelper stopwatchHelper;
    private Runnable runnable = new Runnable() {
        public void run() {
            stopwatchHelper().makeTime();
            binding.circularProgressView.setTimerText(StopWatchUtils.getStopwatch(stopwatchHelper().getTime()));
            binding.circularProgressView.setProgress(((float) (stopwatchHelper().getTime() % 60000)) / 60000.0f);
            handler.postDelayed(this, 10);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStopwatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        stopwatchAct = this;
        this.stopwatchHelper = new StopwatchHelper();
        adapterStopwatch = new StopwatchAdapter(stopwatchAct, stopwatchHelper().arr);
        binding.rvStopwatch.setAdapter(adapterStopwatch);
        binding.rvStopwatch.setLayoutManager(new LinearLayoutManager(StopwatchAct.this, 1, false));
        updateView();
        binding.circularProgressView.setTimerText(StopWatchUtils.getStopwatch(0));
        binding.circularProgressView.setProgress(BitmapDescriptorFactory.HUE_RED);
        this.handler = new Handler();
        binding.mIvLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopwatchHelper().play) {
                    stopwatchHelper().loop();
                    adapterStopwatch.notifyDataSetChanged();
                }
            }
        });
        binding.mIvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!stopwatchHelper().play) {
                    stopwatchHelper().start();
                    binding.mIvRestart.setEnabled(false);
                    handler.post(runnable);
                } else {
                    stopwatchHelper().pause();
                    binding.mIvRestart.setEnabled(true);
                    handler.removeCallbacks(runnable);
                }
                updateView();
            }
        });
        binding.mIvRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopwatchHelper().stop();
                adapterStopwatch.notifyDataSetChanged();
                binding.circularProgressView.setTimerText(StopWatchUtils.getStopwatch(0));
                binding.circularProgressView.setProgress(BitmapDescriptorFactory.HUE_RED);
            }
        });
        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        if (Global.banner_all && Global.isInternetConnected(stopwatchAct)) {
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

    private void onBackCall() {
        finish();
    }

    public void onStart() {
        super.onStart();
        if (stopwatchHelper().play) {
            this.handler.post(this.runnable);
        }
    }

    public void onStop() {
        super.onStop();
        this.handler.removeCallbacks(this.runnable);
    }

    public StopwatchHelper stopwatchHelper() {
        if (stopwatchHelper != null) {
            return stopwatchHelper;
        }
        return new StopwatchHelper();
    }

    private void updateView() {
        if (stopwatchHelper().play) {
            binding.mIvLoop.setClickable(true);
            binding.mIvStart.setImageDrawable(ContextCompat.getDrawable(stopwatchAct, R.drawable.ic_sw_pause));
        } else if (stopwatchHelper().getTime() == 0) {
            binding.mIvStart.setImageDrawable(ContextCompat.getDrawable(stopwatchAct, R.drawable.ic_sw_play));
        } else {
            binding.mIvLoop.setClickable(false);
            binding.mIvStart.setImageDrawable(ContextCompat.getDrawable(stopwatchAct, R.drawable.ic_sw_play));

        }
    }
}