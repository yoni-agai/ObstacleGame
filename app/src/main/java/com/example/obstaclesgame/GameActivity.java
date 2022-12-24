package com.example.obstaclesgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameActivity extends AppCompatActivity {

    private static final String GAME_MODE_SPEED = "GAME_MODE_SPEED";
    private static final String GAME_MODE_SENSOR = "GAME_MODE_SENSOR";
    private static final float GAME_SPEED_FAST = 3000.0f;
    private static final float GAME_SPEED_SLOW = 6000.0f;

    private static final String USER_LATITUDE = "USER_LATITUDE";
    private static final String USER_LONGITUDE = "USER_LONGITUDE";

    private GamePanel gamePanel;
    private FrameLayout game;
    private RelativeLayout gameLayout;
    private FloatingActionButton leftButton, rightButton;

    private boolean isFastSpeed, isSensorMode;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();


        isFastSpeed = getIntent().getBooleanExtra(GAME_MODE_SPEED, false);
        isSensorMode = getIntent().getBooleanExtra(GAME_MODE_SENSOR, false);

        initConstants(isFastSpeed);

        game =  new FrameLayout(this);
        gameLayout = new RelativeLayout(this);

        createButtons();

        latitude = getIntent().getDoubleExtra(USER_LATITUDE, 0L);
        longitude = getIntent().getDoubleExtra(USER_LONGITUDE, 0L);
        gamePanel = new GamePanel(this, leftButton,rightButton, isSensorMode, latitude, longitude);


        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        gameLayout.setLayoutParams(params);
        gameLayout.addView(leftButton);
        gameLayout.addView(rightButton);

        game.addView(gamePanel);
        game.addView(gameLayout);

        setContentView(game);
    }

    public static void launchGame(Context context, boolean isFastSpeed, boolean isSensorMode, double latitude, double longitude) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(GAME_MODE_SPEED, isFastSpeed);
        intent.putExtra(GAME_MODE_SENSOR, isSensorMode);
        intent.putExtra(USER_LATITUDE, latitude);
        intent.putExtra(USER_LONGITUDE, longitude);

        context.startActivity(intent);
    }

    private void initConstants(boolean isFastMode) {

        float gameSpeed;
        System.out.println("ISFASTMODE:" + isFastMode);
        if (isFastMode) {
            gameSpeed = GAME_SPEED_FAST;
        } else {
            gameSpeed = GAME_SPEED_SLOW;
        }
        System.out.println("gamespeed:" + gameSpeed);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        Constants.NUM_OF_ROADS = 5;
        Constants.OBSTACLE_HEIGHT = 100;
        Constants.OBSTACLE_GAP = 500;

        Constants.NUM_OF_LIVES = 3;
        Constants.HEART_SIZE = 150;
        Constants.HEART_GAP = 120;

        Constants.PLAYER_WIDTH = 125;
        Constants.PLAYER_HEIGHT = 125;
        Constants.PLAYER_GAP = Constants.SCREEN_HEIGHT - 800;

        Constants.SPEED = Constants.SCREEN_HEIGHT/gameSpeed;
    }

    private void createButtons() {
        leftButton = new FloatingActionButton(this);
        leftButton.setImageResource((R.drawable.left_button));
        leftButton.setId(R.id.left_fab);

        rightButton = new FloatingActionButton(this);
        rightButton.setImageResource((R.drawable.right_button));
        rightButton.setId(R.id.right_fab);

        setButtonsLayout();

        if (isSensorMode) {
            leftButton.setVisibility(View.GONE);
            rightButton.setVisibility(View.GONE);
        }
    }

    private void setButtonsLayout() {
        RelativeLayout.LayoutParams leftButtonLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams rightButtonLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        leftButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        leftButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        leftButton.setLayoutParams(leftButtonLayout);

        rightButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        rightButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        rightButton.setLayoutParams(rightButtonLayout);
    }
}
