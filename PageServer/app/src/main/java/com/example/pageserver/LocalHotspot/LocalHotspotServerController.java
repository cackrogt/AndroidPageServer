package com.example.pageserver.LocalHotspot;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class LocalHotspotServerController {
    String TAG = "LocalHotspotServerController";
    private WifiManager.LocalOnlyHotspotReservation hotspotReservation;
    public static final int REQUEST_LOCATION_PERMISSION = 1001;
    private LocalHotspotMainActivityCallback mCallback;
    public void startHotspot(WifiManager wifiManager, Context currentContext,
                             LocalHotspotMainActivityCallback callback) {
        mCallback = callback;

        if (ActivityCompat.checkSelfPermission(currentContext,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                        currentContext, Manifest.permission.NEARBY_WIFI_DEVICES)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Ankit, inside localhsc, you get this again");
            ActivityCompat.requestPermissions((Activity)currentContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        else {
            Log.i(TAG, "Ankit start local Hotspot");
            wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
                @Override
                public void onStarted(@NonNull WifiManager.LocalOnlyHotspotReservation reservation) {
                    super.onStarted(reservation);
                    hotspotReservation = reservation;
                    WifiConfiguration config = reservation.getWifiConfiguration();
                    Log.i(TAG, "Hotspot started with SSID: " + config.SSID + " Password: " + config.preSharedKey);
                    String ipLocal = ("Hotspot Started\nSSID: " + config.SSID +
                            "\nPassword: " + config.preSharedKey +
                            "\nVisit http://192.168.43.1:8090");
                    Log.i(TAG, "Ankit start local Hotspot");
                    callback.onResult("success", ipLocal);
                }

                @Override
                public void onStopped() {
                    super.onStopped();
                    Log.d(TAG, "Hotspot stopped");
                    callback.onResult("stopped", "HotspotStopped");
                }

                @Override
                public void onFailed(int reason) {
                    super.onFailed(reason);
                    Log.e(TAG, "Hotspot failed: " + reason);
                    callback.onResult("failure", "Hotspot failed");
                }

            }, null);
        }
    }


}
