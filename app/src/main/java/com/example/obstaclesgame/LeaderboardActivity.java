package com.example.obstaclesgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.obstaclesgame.interfaces.ILeaderboardListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class LeaderboardActivity extends AppCompatActivity implements OnMapReadyCallback, ILeaderboardListener {

    private GoogleMap map;
    private ScoreListFragment score_fragment;

    public static void launchScreen(Context context) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        initGoogleMaps();

        initScoreFragment();
    }

    private void initGoogleMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initScoreFragment() {
        score_fragment =(ScoreListFragment) getSupportFragmentManager().findFragmentById(R.id.leaderboard);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        map.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));

    }

    public void onLeaderboardItemClicked(double latitude, double longitude) {
        LatLng currentScoreLocation = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions()
                .position(currentScoreLocation)
                .title("score location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(currentScoreLocation));
        map.animateCamera(CameraUpdateFactory.zoomIn());
    }
}