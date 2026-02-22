package gps.trackerid.location.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static gps.trackerid.location.adshelper.AdsConfig.getBannerAll;
<<<<<<< HEAD
=======
import static gps.trackerid.location.adshelper.AdsConfig.loadBAckInterstitialAds;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.FileProvider;

import com.ads.module.ads.ERainAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import gps.trackerid.location.R;
<<<<<<< HEAD
=======
import gps.trackerid.location.adshelper.AdsConfig;
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
import gps.trackerid.location.databinding.ActivitySharemyqrBinding;
import gps.trackerid.location.models.users.CurrentUserUtil;
import gps.trackerid.location.models.users.DataUser;
import gps.trackerid.location.ui.baseui.BaseActivity;
import gps.trackerid.location.utils.Global;
import gps.trackerid.location.utils.PreferenceUtil;
import gps.trackerid.location.utils.QrHelper;
import gps.trackerid.location.utils.TrackingCodeUtil;
import kotlin.jvm.internal.Intrinsics;

public class ShareMyQrActivity extends BaseActivity {
    ActivitySharemyqrBinding binding;
    ShareMyQrActivity shareCodeActivity;
    Bitmap qrBitmap;
    String trackingCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySharemyqrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        shareCodeActivity = this;
        binding.mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackCall();
            }
        });
        setupMyCode();
        binding.mIvShareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareText(shareCodeActivity, binding.tvMyCode.getText().toString());
            }
        });
        binding.mIvShareDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnWhatsApp(shareCodeActivity, binding.tvMyCode.getText().toString());
            }
        });
        binding.mIvShareQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareQr();
            }
        });
        binding.ivCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyToClipboard(shareCodeActivity, binding.tvMyCode.getText().toString());
            }
        });
        if (Global.banner_all && Global.isInternetConnected(shareCodeActivity)) {
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
<<<<<<< HEAD
        finish();
=======
       finish();
>>>>>>> f5e5efa8f659ab985326e6f2884b0dd0d70cbc9e
    }

    private void setupMyCode() {

        // 2️⃣ Get SharedPreferences
        SharedPreferences prefs = PreferenceUtil.getGpsTrackersPreferences(shareCodeActivity);
        String registrationCode = prefs.getString("randomRegistrationCode", "");

        // 3️⃣ If empty, generate a unique ID and save it
        if (registrationCode.isEmpty()) {
            registrationCode = TrackingCodeUtil.getPhoneUniqueId(shareCodeActivity);
            prefs.edit().putString("randomRegistrationCode", registrationCode).apply();
        }
        trackingCode = registrationCode;

// show code
        binding.tvMyCode.setText(trackingCode);

// generate QR
        qrBitmap = QrHelper.generateQrCode(trackingCode, 500, 500);
        binding.myQrCode.setImageBitmap(qrBitmap);

    }


    public final void copyToClipboard(Context context, String text) {
        Object systemService = context.getSystemService("clipboard");
        ((ClipboardManager) systemService).setPrimaryClip(ClipData.newPlainText("Copied Text", text));
        Toast.makeText(context, getResources().getString(R.string.copy_clip), 0).show();
    }

    private void shareQr() {
        String code;
        binding.progressShareCode.setVisibility(VISIBLE);
        DataUser currentUser = CurrentUserUtil.getCurrentUser(shareCodeActivity);
        if (currentUser == null || (code = currentUser.getCode()) == null) {
            code = "";
        }
        if (qrBitmap == null) {
            qrBitmap = QrHelper.generateQrCode(trackingCode, 500, 500);
        }

        if (qrBitmap != null) {
            try {
                new Thread(new Runnable() { // from class: com.live.location.gpsTracker.presentation.activity.ShareCodeActivity$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            shareQrImage(qrBitmap);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, getResources().getString(R.string.faile_qr), 0).show();
            }
        }
    }

    private void shareQrImage(Bitmap bitmap) throws FileNotFoundException {
        File file = new File(getCacheDir(), "shared_images");
        file.mkdirs();
        File file2 = new File(file, "qr_code.png");
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            final Uri uriForFile = FileProvider.getUriForFile(shareCodeActivity, shareCodeActivity.getPackageName() + ".provider", file2);
            Intrinsics.checkNotNullExpressionValue(uriForFile, "getUriForFile(...)");
            shareCodeActivity.runOnUiThread(new Runnable() {
                @Override
                public final void run() {
                    shareQrImageFile(uriForFile);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
        }
    }

    private void shareQrImageFile(Uri uriForFile) {
        binding.progressShareCode.setVisibility(GONE);
        StringBuilder sb = new StringBuilder("Here's my QR Code! 📱\n\nDownload our app from Play Store:\n");
        sb.append("https://play.google.com/store/apps/details?id=" + shareCodeActivity.getPackageName());
        String string = sb.toString();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        intent.putExtra("android.intent.extra.TEXT", string);
        intent.addFlags(1);
        shareCodeActivity.startActivity(Intent.createChooser(intent, "Share QR Code"));
    }

    public static final void shareOnWhatsApp(Activity activity, String text) {
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.setPackage("com.whatsapp");
            intent.putExtra("android.intent.extra.TEXT", text);
            activity.startActivity(intent);
        } catch (Exception unused) {
            shareText(activity, text);
        }
    }

    public static final void shareText(Context context, String text) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", text);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "Share app via"));
    }
}