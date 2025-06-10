package com.example.pageserver;

import static com.example.pageserver.LocalHotspot.LocalHotspotServerController.REQUEST_LOCATION_PERMISSION;

import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.example.pageserver.LocalHotspot.LocalHotspotMainActivityCallback;
import com.example.pageserver.LocalHotspot.LocalHotspotServerController;
import com.example.pageserver.webserver.NanoWebServer;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pageserver.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private LocalHotspotServerController localHotspotServerController;

    NanoWebServer server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int port = 8090;
        binding.StartServer.setOnClickListener(view -> {
            try {
                server = new NanoWebServer(port);
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                binding.urlview.setText(ip + ":" + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        binding.StartLocalHotspotServer.setOnClickListener(view->{
            LocalHotspotMainActivityCallback callback = (result, ipLocal) -> {
                switch (result) {
                    case "success":
                        try {
                            getLocalIpAddress();
                            server = new NanoWebServer(port);
                            binding.urlview.setText(ipLocal);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    case "failure":
                        binding.urlview.setText(ipLocal);
                    case "stopped":
                        binding.urlview.setText("hotspot stopped");
                }
            };
            localHotspotServerController = new LocalHotspotServerController();
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            localHotspotServerController.startHotspot(wm,
                    this, callback);
        });
        binding.StopServer.setOnClickListener(view -> {
            if (server != null) {
                server.stop();
                server = null;
            }
            binding.urlview.setText("No Url Served");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ip = inetAddress.getHostAddress();
                        if (ip.startsWith("192.168.")) {
                            Log.i("MainActivity", "Ankit the ip is: " + ip);
                            return ip + ":8090";
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        Log.i("MainActivity" , "ip Unknown");
        return "ipNotFound";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("MainActivity", "Ankit the request result is: " + requestCode + " "
                + (permissions.length > 0 ? permissions[0] : "none") + " "
                + (grantResults.length > 0 ? grantResults[0] : "none"));
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    binding.urlview.setText(getLocalIpAddress());
                    server = new NanoWebServer(8090);
                } catch (IOException e) {
                    Log.i("MainActivity", "we have IO exception");
                }
            } else {
                binding.urlview.setText("Permission denied: Cannot start hotspot.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}