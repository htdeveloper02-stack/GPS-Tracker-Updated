package gps.trackerid.location.ui.phonetracker;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ads.module.ads.ERainAd;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gps.trackerid.location.R;
import gps.trackerid.location.adapter.AreaAdapter;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.databinding.AreaCodesBinding;
import gps.trackerid.location.models.AreaModel;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class AreaCodes extends BaseActivity {
    AreaCodesBinding areaCodesBinding;
    String mSelectedCountry;
    AreaAdapter areaAdapter;
    ArrayList<AreaModel> allAreas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaCodesBinding = AreaCodesBinding.inflate(getLayoutInflater());
        setContentView(areaCodesBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        areaCodesBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        areaAdapter = new AreaAdapter(AreaCodes.this);
        areaCodesBinding.mRvCity.setLayoutManager(new GridLayoutManager(this, 1));
        areaCodesBinding.mRvCity.setAdapter(areaAdapter);
        areaCodesBinding.mCCodePicker.setCountryForNameCode("us");
        mSelectedCountry = areaCodesBinding.mCCodePicker.getSelectedCountryName();
        areaCodesBinding.mCCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                if (filterByCountry(areaCodesBinding.mCCodePicker.getSelectedCountryName()).isEmpty()) {
                    areaCodesBinding.tvEmptyData.setVisibility(VISIBLE);
                    areaCodesBinding.mRvCity.setVisibility(GONE);
                } else {
                    areaCodesBinding.tvEmptyData.setVisibility(GONE);
                    areaCodesBinding.mRvCity.setVisibility(VISIBLE);
                    areaAdapter.setAreas(filterByCountry(areaCodesBinding.mCCodePicker.getSelectedCountryName()), areaCodesBinding.mEtCitySearch.getText().toString());
                }
            }
        });
        areaCodesBinding.mEtCitySearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(areaCodesBinding.mEtCitySearch.getWindowToken(), 0);

                    // Call your search method here
//                    String query = areaCodesBinding.mEtCitySearch.getText().toString().trim();
//                    areaAdapter.filter(query);

                    return true; // handled
                }
                return false;
            }
        });
        areaCodesBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = areaCodesBinding.mEtCitySearch.getText().toString();
                if (str.isEmpty()) {
                    Toast.makeText(AreaCodes.this, getResources().getString(R.string.enter_city), Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(areaCodesBinding.mEtCitySearch.getWindowToken(), 0);

                    areaAdapter.setAreas(filterByCountry(areaCodesBinding.mCCodePicker.getSelectedCountryName()), str);
                }
            }
        });
        areaCodesBinding.mEtCitySearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                areaAdapter.filter(charSequence.toString());
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                String[] items = getResources().getStringArray(R.array.std_array);

                for (String item : items) {
                    String[] parts = item.split(" - ");
                    if (parts.length == 3) {
                        allAreas.add(new AreaModel(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                    }
                }

                // 3. UI update (same as onPostExecute)
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (allAreas.isEmpty()) {
                            areaCodesBinding.tvEmptyData.setVisibility(VISIBLE);
                            areaCodesBinding.mRvCity.setVisibility(GONE);
                        } else {
                            areaCodesBinding.tvEmptyData.setVisibility(GONE);
                            areaCodesBinding.mRvCity.setVisibility(VISIBLE);
                            areaAdapter.setAreas(allAreas, "");
                        }
                    }
                });
            }
        });
        if (Global.banner_all && Global.isInternetConnected(AreaCodes.this)) {
            areaCodesBinding.mRlBanner.setVisibility(VISIBLE);
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

    public ArrayList<AreaModel> filterByCountry(String selectedCountry) {
        ArrayList<AreaModel> filtered = new ArrayList<>();

        for (AreaModel model : allAreas) {
            if (model.country.equalsIgnoreCase(selectedCountry)) {
                filtered.add(model);
            }
        }

        return filtered;
    }

}