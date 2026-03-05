package gps.trackerid.location.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.hbb20.CountryCodePicker;

import gps.trackerid.location.R;
import gps.trackerid.location.ads.AdsManager;
import gps.trackerid.location.ads.RemoteUtils;
import gps.trackerid.location.databinding.ActivityPhonelocatorBinding;
import gps.trackerid.location.models.PhoneCarrierInfo;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.ui.phonetracker.PhoneDetails;
import gps.trackerid.location.utils.CarrierDetector;

public class PhoneLocator extends BaseActivity implements OnMapReadyCallback {
    ActivityPhonelocatorBinding phonelocatorBinding;
    PhoneLocator phoneLocator;
    private GoogleMap googleMap;
    private LocationListener locationListener;
    private String fullPhoneNumber;
    ShimmerFrameLayout shimmerAds;
    public boolean isShortcut = false;

    public String removeCountryCodeIfExists(String number) {

        // Remove all non-digit characters
        number = number.replaceAll("[^\\d]", "");

        // Example: 911111199999 -> 1111199999
        // We remove country code only if it looks like it starts with 1-3 digit country code

        // Most country codes are 1 to 3 digits
        for (int len = 1; len <= 3; len++) {
            if (number.length() > len) {
                String possibleCode = number.substring(0, len);
                String remaining = number.substring(len);

                // If remaining length is 10 (common mobile length), remove country code
                if (remaining.length() == 10) {
                    return remaining;
                }
            }
        }

        // If no country code found, return original number
        return number;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phonelocatorBinding = ActivityPhonelocatorBinding.inflate(getLayoutInflater());
        setContentView(phonelocatorBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        phoneLocator = this;
        handleShortcutIntent(getIntent());

        phonelocatorBinding.mapView.onCreate(null);
        phonelocatorBinding.mapView.getMapAsync(this);

        if (getIntent().hasExtra("phoneNumber")) {
            String Number = getIntent().getStringExtra("phoneNumber");
            assert Number != null;
            if (!Number.isEmpty()) {
                phonelocatorBinding.etPhoneNumber.setText(removeCountryCodeIfExists(Number));
            }
        }

        phonelocatorBinding.mCCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                if (!phonelocatorBinding.etPhoneNumber.getText().toString().isEmpty()) {
                    String phoneNumber = phonelocatorBinding.etPhoneNumber.getText().toString().trim();
                    if (!phoneNumber.isEmpty()) {
                        searchPhoneNumber();
                    } else {
                        Toast.makeText(phoneLocator, getResources().getString(R.string.enter_phone), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        phonelocatorBinding.ivSearch.setOnClickListener(v -> {
            hideKeyboard();
            String phoneNumber = phonelocatorBinding.etPhoneNumber.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                searchPhoneNumber();
            } else {
                Toast.makeText(phoneLocator, getResources().getString(R.string.enter_phone), Toast.LENGTH_SHORT).show();
            }
        });

        phonelocatorBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackCall();
            }
        });

        AdsManager.INSTANCE.loadNativePhoneLocator(this, phonelocatorBinding.frAds);
    }

    private void onBackCall() {
        AdsManager.INSTANCE.showInterBack(PhoneLocator.this, () -> {
            if (isShortcut) {
                startActivity(new Intent(phoneLocator, TimestampActivity.class));
                finish();
            } else {
                finish();
            }
            return null;
        });
    }

    private void searchPhoneNumber() {

        // For demonstration, we just show a static location & fake carrier info
        // Replace this with a real API lookup (like numverify.com, etc.)
        String inputNumber = phonelocatorBinding.etPhoneNumber.getText().toString().trim();
        String countryCode = phonelocatorBinding.mCCodePicker.getSelectedCountryCodeWithPlus();

        fullPhoneNumber = countryCode + inputNumber;
        String countryIso = phonelocatorBinding.mCCodePicker.getSelectedCountryNameCode();
// Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(phonelocatorBinding.etPhoneNumber.getWindowToken(), 0);
        }
        if (isValidPhone(fullPhoneNumber)) {
            PhoneCarrierInfo countryloaction = CarrierDetector.detect(phoneLocator, fullPhoneNumber);
            if (countryloaction != null) {
                String nationnum = "".concat(fullPhoneNumber);
                String carrierName = countryloaction.getCarrier();
                // Open details activity
                Intent intent = new Intent(this, PhoneDetails.class);
                intent.putExtra("phoneNumber", fullPhoneNumber);
                intent.putExtra("countryIso", countryIso);
                intent.putExtra("countryLocation", countryloaction.getCountry());
                intent.putExtra("nationalNumber", nationnum);
                intent.putExtra("carrierName", carrierName);
                startActivity(intent);
            } else {
                String nationnum = "".concat(fullPhoneNumber);
                String carrierName = "";
                // Open details activity
                Intent intent = new Intent(this, PhoneDetails.class);
                intent.putExtra("phoneNumber", fullPhoneNumber);
                intent.putExtra("countryIso", countryIso);
                intent.putExtra("countryLocation", "");
                intent.putExtra("nationalNumber", nationnum);
                intent.putExtra("carrierName", carrierName);
                startActivity(intent);
                Toast.makeText(phoneLocator, getResources().getString(R.string.not_number), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(phoneLocator, getResources().getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidPhone(String inputNumber) {

        if (inputNumber == null || inputNumber.trim().isEmpty()) {
            return false;
        }

        // Keep digits only
        String number = inputNumber.replaceAll("\\D", "");

        // Remove international prefix
        if (number.startsWith("00")) {
            number = number.substring(2);
        }

        // Hard limit (E.164 max length)
        if (number.length() < 7 || number.length() > 15) {
            return false;
        }

        // ❌ Reject repeated digits like 111111, 0000000
        if (number.matches("(\\d)\\1+")) {
            return false;
        }

        // ❌ Reject simple sequential numbers
        if (isSequential(number)) {
            return false;
        }

        return true;
    }

    private boolean isSequential(String number) {
        boolean ascending = true;
        boolean descending = true;

        for (int i = 1; i < number.length(); i++) {
            int diff = number.charAt(i) - number.charAt(i - 1);
            if (diff != 1) ascending = false;
            if (diff != -1) descending = false;
        }

        return ascending || descending;
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public class PrefixModel {
        private String prefix;
        private String country;
        private String carrier;

        public PrefixModel(String prefix, String country, String carrier) {
            this.prefix = prefix;
            this.country = country;
            this.carrier = carrier;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        phonelocatorBinding.mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        phonelocatorBinding.mapView.onStart();
    }

    @Override
    protected void onPause() {
        phonelocatorBinding.mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        phonelocatorBinding.mapView.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        phonelocatorBinding.mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        phonelocatorBinding.mapView.onLowMemory();
    }

    private void handleShortcutIntent(Intent intent) {
        if (intent != null && "android.intent.action.SHORTCUT_PHONE_LOCATOR".equals(intent.getAction())) {
            // Open the VPN Server screen
            isShortcut = true;
            RemoteUtils.INSTANCE.init(() -> {
                AdsManager.INSTANCE.loadNativePhoneLocator(this, phonelocatorBinding.frAds);
            });
        } else {
            isShortcut = false;
        }

    }
}