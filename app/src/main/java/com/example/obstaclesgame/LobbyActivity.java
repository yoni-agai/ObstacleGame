package com.example.obstaclesgame;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class LobbyActivity extends AppCompatActivity  {

    private FusedLocationProviderClient fusedLocationClient;
    private static double latitude, longitude;
    private Button startGame, recordsTable;
    private CheckBox fastMode, sensorMode;
    private boolean isFastSpeed;
    private boolean isSensorMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);
        initViews();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        @SuppressLint("MissingPermission") ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        fineLocationGranted = result.getOrDefault(
                                Manifest.permission.ACCESS_FINE_LOCATION, false);
                    }
                    Boolean coarseLocationGranted = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        coarseLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_COARSE_LOCATION,false);
                    }
                    if (fineLocationGranted != null && fineLocationGranted) {

                                // Precise location access granted.
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                // Got last known location. In some rare situations this can be null.
                                                if (location != null) {
                                                    // Logic to handle location object
                                                    latitude =  location.getLatitude();
                                                    longitude = location.getLongitude();
                                                }
                                            }
                                        });

                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                fusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                // Got last known location. In some rare situations this can be null.
                                                if (location != null) {
                                                    latitude =  location.getLatitude();
                                                    longitude = location.getLongitude();
                                                }
                                            }
                                        });
                            } else {
                                // No location access granted.
                            }
                        }
                );

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void initViews() {

        startGame = findViewById(R.id.start_game);
        recordsTable = findViewById(R.id.records_table);
        fastMode = findViewById(R.id.fast_mode);
        sensorMode = findViewById(R.id.sensor_mode);

        startGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isFastSpeed = fastMode.isChecked();
                isSensorMode = sensorMode.isChecked();
                GameActivity.launchGame(LobbyActivity.this, isFastSpeed, isSensorMode, latitude, longitude);
            }
        });

        recordsTable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LeaderboardActivity.launchScreen(LobbyActivity.this);
            }
        });
    }
}
