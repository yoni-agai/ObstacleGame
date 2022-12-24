package com.example.obstaclesgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final long COIN_SCORE = 100;

    private MainThread thread;
    private Context context;
    private Vibrator vibrator;
    private MediaPlayer collisionSound;

    private Player player;
    private Point playerPoint;
    private ArrayList<PlayerHealth> lives;

    private ObstacleManager obstacleManager;

    private Drawable heartDrawable;
    private Drawable carDrawable;
    private Drawable wallDrawable;
    private Drawable coinDrawable;

    private HighScore highScore;
    private Paint scorePaint;
    private long numCoinCollected;
    private long scoreTime;
    private long scoreTotal;

    private FloatingActionButton leftButton, rightButton;

    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean isSensorMode;

    private double  latitude, longitude;

    enum PlayerOrientation {
      LEFT,
      RIGHT,
      STAND_STILL
    };

    private PlayerOrientation current_player_orientation;

    public GamePanel(Context context, FloatingActionButton leftButton,
                     FloatingActionButton rightButton, boolean isSensorMode, double latitude, double longitude) {
        super(context);
        this.context = context;
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.collisionSound = MediaPlayer.create(context, R.raw.crash);

        this.leftButton = leftButton;
        this.rightButton = rightButton;

        this.isSensorMode = isSensorMode;

        this.latitude = latitude;
        this.longitude = longitude;

        this.scorePaint = new Paint();
        this.scorePaint.setTextSize(100);

        this.numCoinCollected = 0;
        this.scoreTime = System.currentTimeMillis();


        getHolder().addCallback(this);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        carDrawable = context.getResources().getDrawable(R.drawable.car);
        player = new Player(new Rect(0, 0,
                Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT), carDrawable);
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.PLAYER_GAP);

        lives = new ArrayList<>();
        heartDrawable = context.getResources().getDrawable(R.drawable.heart);
        initLives();

        wallDrawable = context.getResources().getDrawable(R.drawable.wall);
        coinDrawable = context.getResources().getDrawable(R.drawable.coin);
        obstacleManager = new ObstacleManager(Constants.OBSTACLE_GAP,
                Constants.NUM_OF_ROADS,
                Constants.OBSTACLE_HEIGHT, wallDrawable, coinDrawable);

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    private void initLives() {
        int startX = Constants.SCREEN_WIDTH - Constants.HEART_GAP;
        for (int i = 0; i < Constants.NUM_OF_LIVES; i++) {
            lives.add(i, new PlayerHealth(startX, 0,
                    Constants.HEART_SIZE, Constants.HEART_SIZE, heartDrawable));
            startX -= Constants.HEART_GAP;
        }
    }

    public void update() {

        if (isSensorMode) {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            leftButton.setOnClickListener(v ->
                    playerPoint.set(player.getRectangle().left, Constants.PLAYER_GAP));
            rightButton.setOnClickListener(v ->
                    playerPoint.set(player.getRectangle().right, Constants.PLAYER_GAP));
        }

        if (current_player_orientation == PlayerOrientation.LEFT) {
            playerPoint.set(player.getRectangle().left + player.getRectangle().width() / 3, Constants.PLAYER_GAP);
        } else if (current_player_orientation == PlayerOrientation.RIGHT) {
            playerPoint.set(player.getRectangle().right - player.getRectangle().width() / 3, Constants.PLAYER_GAP);
        } else {
            // Do nothing stand still
        }

        player.update(playerPoint);
        obstacleManager.update();

        updateScore();

        if (obstacleManager.playerCollide(player)) {

            boolean isWallCollision = obstacleManager.isWallCollision();

            if (isWallCollision ) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        collisionSound.start();
                        Toast toast = Toast.makeText(context, "Collision Accrued", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                //vibrator.vibrate(300));
                lives.remove(lives.get(lives.size() - 1));

                if (lives.size() == 0) {
                    finishGame();
                }
            }
            //coin collision
            else
            {
                numCoinCollected++;
            }
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float z = event.values[2];
            float ROTATION_ANGLE = 15;

            // turn right
            if (z < -ROTATION_ANGLE) {
                current_player_orientation = PlayerOrientation.RIGHT;
                System.out.println("turn righ");
            }
            // turn left
            else if (z > ROTATION_ANGLE) {
                current_player_orientation = PlayerOrientation.LEFT;
                System.out.println("turn left");
            } else {
                System.out.println("stand still");
                current_player_orientation = PlayerOrientation.STAND_STILL;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.GRAY);

        player.customDraw(canvas);
        obstacleManager.customDraw(canvas);

        for (PlayerHealth life : lives) {
            life.customDraw(canvas);
        }

        String totalScoreStr = String.valueOf(scoreTotal);
        canvas.drawText(totalScoreStr, 0, 110,scorePaint);
    }

    private void updateScore() {

        long scoreElapsedTime = (System.currentTimeMillis() - scoreTime);
        long timeScore = (long)(scoreElapsedTime * (Constants.SPEED/20));
        long coinScore = numCoinCollected * COIN_SCORE;
        long totalScore = timeScore + coinScore;
        scoreTotal = totalScore;
    }


    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void finishGame() {
        long lowestHighScore = SharedPreferencesManager.getLowestScore(context);
        int playersCount = SharedPreferencesManager.getPlayerCount(context);

        if (lowestHighScore > scoreTotal && playersCount > 10) {
            LeaderboardActivity.launchScreen(context);
            return;
        }

        ArrayList<HighScore> leaderboard = SharedPreferencesManager.getLeaderBoards(context);
        highScore = new HighScore(latitude, longitude, scoreTotal);

        if (leaderboard.size() > 10) {
            leaderboard.remove(leaderboard.size() - 1);
        }

        leaderboard.add(highScore);

        if (!leaderboard.isEmpty()) {
            Collections.sort(leaderboard, new Comparator<HighScore>() {
                @Override
                public int compare(HighScore h1, HighScore h2) {
                    if( h1.getScore() < h2.getScore()) {
                        return 1;
                    } else if (h1.getScore() > h2.getScore()){
                        return -1;
                    }
                    return 0;
                }
            });
        }

        String json = new Gson().toJson(leaderboard);
        SharedPreferencesManager.saveNewLeaderBoards(context, json);
        SharedPreferencesManager.saveNewHighestScore(context, leaderboard.get(0).getScore());
        SharedPreferencesManager.saveNewLowestScore(context, leaderboard.get(leaderboard.size() - 1).getScore());
        SharedPreferencesManager.savePlayerCount(context, leaderboard.size());

        LeaderboardActivity.launchScreen(context);
    }
}

