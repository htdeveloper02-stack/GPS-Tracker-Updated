package gps.trackerid.location.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import gps.trackerid.location.R;
import gps.trackerid.location.database.FirebaseUserHelper;
import gps.trackerid.location.models.users.CurrentUserUtil;
import gps.trackerid.location.models.users.DataUser;

public class LocationUpdateService extends Service {

    private static volatile boolean isRunning = false;

    private static final String CHANNEL_ID = "location_update_channel";
    private static final int NOTIFICATION_ID = 1;

    // 5 seconds (replace with your own value if needed)
    private static final long DELAY_MILLIS = 5000;

    private FusedLocationProviderClient fusedLocationClient;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable locationRunnable = new Runnable() {
        @Override
        public void run() {
            checkAndUpdateLocation();
            handler.postDelayed(this, DELAY_MILLIS);
        }
    };

    // ===============================
    // Service lifecycle
    // ===============================

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundServiceNotification();
        handler.removeCallbacks(locationRunnable);
        handler.post(locationRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        handler.removeCallbacks(locationRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ===============================
    // Foreground notification
    // ===============================

    private void startForegroundServiceNotification() {
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Updates",
                    NotificationManager.IMPORTANCE_LOW
            );

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("GPS Tracker")
                        .setContentText("Location sharing is running")
                        .setSmallIcon(R.drawable.ic_marker_add)
                        .setOngoing(true);

//        manager.notify(NOTIFICATION_ID, notification.build());
        startForeground(NOTIFICATION_ID, notification.build());
    }

    // ===============================
    // Location handling
    // ===============================

    private void checkAndUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.FOREGROUND_SERVICE_LOCATION) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    updateUserLocation(location);
                }
            }
        });
    }

    private void updateUserLocation(Location location) {
        DataUser currentUser = CurrentUserUtil.getCurrentUser(this);
        if (currentUser == null) {
            return;
        }

        DataUser updatedUser = new DataUser(
                currentUser.getCode(),
                currentUser.getName(),
                location.getLatitude(),
                location.getLongitude(),
                currentUser.isSelected(),
                currentUser.isSharing(),
                currentUser.getFcmToken()
        );
        FirebaseUserHelper.INSTANCE.updateUser(updatedUser);
    }

    // ===============================
    // Public helper
    // ===============================

    public static boolean isRunning() {
        return isRunning;
    }
}

