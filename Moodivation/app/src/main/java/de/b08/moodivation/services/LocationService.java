package de.b08.moodivation.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

import de.b08.moodivation.R;
import de.b08.moodivation.database.location.LocationDatabase;
import de.b08.moodivation.database.location.entities.LocationHistoryEntity;

public class LocationService extends Service implements LocationListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public interface Constants {
        String LOCATION_SERVICE_ENABLED_SETTING = "LocationServiceEnabled";
        boolean LOCATION_SERVICE_ENABLED_DEFAULT_VALUE = false;

        String LOCATION_SERVICE_MIN_DISTANCE_M_SETTING = "LocationServiceMinDistanceM";
        float LOCATION_SERVICE_MIN_DISTANCE_M_DEFAULT_VALUE = 0;

        String LOCATION_SERVICE_MIN_TIME_MS_SETTING = "LocationServiceMinTimeMs";
        Frequency LOCATION_SERVICE_MIN_TIME_MS_DEFAULT_VALUE = Frequency.FAST;

        String LOCATION_SERVICE_ACCURACY_SETTING = "LocationServiceAccuracy";
        Accuracy LOCATION_SERVICE_ACCURACY_DEFAULT_VALUE = Accuracy.GPS;
    }

    public enum Accuracy {
        GPS(LocationManager.GPS_PROVIDER),
        NETWORK(LocationManager.NETWORK_PROVIDER);

        private final String value;

        Accuracy(String val) {
            this.value = val;
        }

        public String getValue() {
            return value;
        }

        public static Accuracy getAccuracyFor(String value) {
            return Arrays.stream(values()).filter(d -> Objects.equals(d.value, value)).findFirst()
                    .orElse(Constants.LOCATION_SERVICE_ACCURACY_DEFAULT_VALUE);
        }
    }

    public enum Frequency {
        FAST(0),
        SLOW(1000);

        private final long value;

        Frequency(long val) {
            this.value = val;
        }

        public long getValue() {
            return value;
        }

        public static Frequency getFrequencyFor(long value) {
            return Arrays.stream(values()).filter(d -> d.value == value).findFirst()
                    .orElse(Constants.LOCATION_SERVICE_MIN_TIME_MS_DEFAULT_VALUE);
        }
    }

    private static final int BATCH_SIZE = 50;
    // private static final int TRIES = 3;
    private LinkedList<Location> locations;

    private LocationManager locationManager;

    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        sharedPreferences = getApplicationContext().getSharedPreferences("TimeSettings", Context.MODE_PRIVATE);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        locations = new LinkedList<>();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        saveBatch();
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifyUser();
        registerListener();

        return START_STICKY;
    }

    public void registerListener() {
        if (!sharedPreferences.getBoolean(Constants.LOCATION_SERVICE_ENABLED_SETTING, Constants.LOCATION_SERVICE_ENABLED_DEFAULT_VALUE))
            return;

        Accuracy accuracy = Accuracy.getAccuracyFor(sharedPreferences.getString(Constants.LOCATION_SERVICE_ACCURACY_SETTING,
                Constants.LOCATION_SERVICE_ACCURACY_DEFAULT_VALUE.getValue()));
        float minDistanceM = sharedPreferences.getFloat(Constants.LOCATION_SERVICE_MIN_DISTANCE_M_SETTING,
                Constants.LOCATION_SERVICE_MIN_DISTANCE_M_DEFAULT_VALUE);
        long minTimeMs = sharedPreferences.getLong(Constants.LOCATION_SERVICE_MIN_TIME_MS_SETTING,
                Constants.LOCATION_SERVICE_MIN_TIME_MS_DEFAULT_VALUE.getValue());

        if (accuracy == Accuracy.GPS) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeMs, minDistanceM, this);
            }
        }

        // NETWORK < GPS => NETWORK always on
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeMs, minDistanceM, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locations.add(location);
        saveBatchIfNecessary();
    }

    protected void saveBatchIfNecessary() {
        if (locations.size() > BATCH_SIZE) {
            saveBatch();
        }
    }

    public void saveBatch() {
        if (locations == null)
            return;

        LinkedList<Location> observationsCopy = new LinkedList<>(locations);
        locations.clear();
        AsyncTask.execute(() -> {
            if (observationsCopy.isEmpty())
                return;

            LocationDatabase.getInstance(getApplicationContext()).locationHistoryDao()
                    .insertAll(LocationHistoryEntity.from(observationsCopy));
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!Arrays.asList(Constants.LOCATION_SERVICE_MIN_DISTANCE_M_SETTING, Constants.LOCATION_SERVICE_ENABLED_SETTING,
                Constants.LOCATION_SERVICE_ACCURACY_SETTING, Constants.LOCATION_SERVICE_MIN_TIME_MS_SETTING).contains(key))
            return;

        locationManager.removeUpdates(this);
        registerListener();
    }

    private void notifyUser() {
        String channelId = "LOC_SERVICE_NOTIFICATION_CHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(channelId,
                "Notification channel for location service", NotificationManager.IMPORTANCE_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(getString(R.string.locationServiceForegroundNotificationTitle))
                .setContentText(getString(R.string.locationServiceForegroundNotificationText))
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .setAutoCancel(false)
                .build();

        startForeground(1, notification);
    }

}
