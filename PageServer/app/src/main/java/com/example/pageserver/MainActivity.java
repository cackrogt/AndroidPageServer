package com.example.pageserver;

import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.example.pageserver.webserver.NanoWebServer;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.format.Formatter;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pageserver.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
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
}