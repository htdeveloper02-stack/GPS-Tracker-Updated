package gps.trackerid.location.ui.phonetracker;

import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;

import com.ads.module.ads.ERainAd;

import gps.trackerid.location.R;
import gps.trackerid.location.adshelper.AdsConfig;
import gps.trackerid.location.databinding.ActivityPhonedetailsBinding;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;

public class PhoneDetails extends BaseActivity {
    ActivityPhonedetailsBinding phonedetailsBinding;
    PhoneDetails phoneDetails;
    private String phoneNumber;
    private String countryIso;
    private String countryLocation;
    private String nationalNumber;
    private String carrierName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phonedetailsBinding = ActivityPhonedetailsBinding.inflate(getLayoutInflater());
        setContentView(phonedetailsBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        phoneDetails = this;

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        countryIso = getIntent().getStringExtra("countryIso");
        countryLocation = getIntent().getStringExtra("countryLocation");
        nationalNumber = getIntent().getStringExtra("nationalNumber");
        carrierName = getIntent().getStringExtra("carrierName");

        phonedetailsBinding.tvISONumber.setText(countryIso);
        phonedetailsBinding.countryLocationTextView.setText(countryLocation);
        phonedetailsBinding.tvNationalNumber.setText(nationalNumber);
        phonedetailsBinding.countryTypeTextView.setText(carrierName);

        phonedetailsBinding.mIvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMethodCall(1);
            }
        });
        phonedetailsBinding.mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMethodCall(3);
            }
        });
        phonedetailsBinding.mIvSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMethodCall(2);
            }
        });

        phonedetailsBinding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        if (ActivityCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") == 0) {
            String str = null;
            if (phoneNumber != null && ActivityCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS") == 0) {
                Cursor query = getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)), new String[]{"display_name"}, null, null, null);
                if (query != null) {
                    Cursor cursor = query;
                    try {
                        Cursor cursor2 = cursor;
                        if (cursor2.moveToFirst()) {
                            str = cursor2.getString(cursor2.getColumnIndexOrThrow("display_name"));
                            cursor.close();
                        } else {
                            cursor.close();
                        }
                    } finally {
                    }
                }
            }
            if (str == null) {
                str = "Unknown";
            }
            phonedetailsBinding.mTxtName.setText(str);
        } else {
            phonedetailsBinding.mTxtName.setText("Unknown");
        }
        if (Global.banner_all && Global.isInternetConnected(phoneDetails)) {
            phonedetailsBinding.mRlBanner.setVisibility(VISIBLE);
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

    private void mMethodCall(int i) {
        switch (i) {
            case 2:
                mCallSms();
                break;
            case 3:
                mCallAddContact();
                break;
            default:
                mCallNumber();
                break;
        }
    }

    private void mCallNumber() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(phoneDetails, getResources().getString(R.string.fail_call)+" " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void mCallAddContact() {
        // Normalize number
        String normalized = phoneNumber.replaceAll("\\s+", "");

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String existing = cursor.getString(0).replaceAll("\\s+", "");
                    if (existing.equals(normalized)) {
                        Toast.makeText(phoneDetails, getResources().getString(R.string.already_contact), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            // Open insert contact screen
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(phoneDetails, getResources().getString(R.string.fail_add_contact)+" " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    private void mCallSms() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + phoneNumber));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(phoneDetails, getResources().getString(R.string.fail_add_sms)+" " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        phoneDetails = null;
    }
}