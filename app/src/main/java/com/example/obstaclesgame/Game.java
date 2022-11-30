package com.example.obstaclesgame;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Game extends AppCompatActivity {

    private GamePanel gamePanel;
    private FrameLayout game;
    private RelativeLayout gameButtons;
    private FloatingActionButton leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        initConstants();

        game =  new FrameLayout(this);
        gameButtons = new RelativeLayout(this);

        createButtons();

        gamePanel = new GamePanel(this, leftButton,rightButton);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        gameButtons.setLayoutParams(params);
        gameButtons.addView(leftButton);
        gameButtons.addView(rightButton);

        game.addView(gamePanel);
        game.addView(gameButtons);

        setContentView(game);
    }

    private void initConstants() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH = dm.widthPixels;
        Constants.SCREEN_HEIGHT = dm.heightPixels;

        Constants.NUM_OF_ROADS = 3;
        Constants.OBSTACLE_HEIGHT = 200;
        Constants.OBSTACLE_GAP = 500;

        Constants.NUM_OF_LIVES = 3;
        Constants.HEART_SIZE = 150;
        Constants.HEART_GAP = 120;

        Constants.PLAYER_WIDTH = 250;
        Constants.PLAYER_HEIGHT = 250;
        Constants.PLAYER_GAP = Constants.SCREEN_HEIGHT - 800;

        Constants.SPEED = Constants.SCREEN_HEIGHT/10000.0f;
    }

    private void createButtons() {
        leftButton = new FloatingActionButton(this);
        leftButton.setImageResource((R.drawable.left_button));
        leftButton.setId(R.id.left_fab);

        rightButton = new FloatingActionButton(this);
        rightButton.setImageResource((R.drawable.right_button));
        rightButton.setId(R.id.right_fab);

        setButtonsLayout();
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
