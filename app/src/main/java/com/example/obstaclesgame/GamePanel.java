package com.example.obstaclesgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Context context;
    private Vibrator vibrator;

    private Player player;
    private Point playerPoint;
    private ArrayList<PlayerHealth> lives;

    private ObstacleManager obstacleManager;

    private Drawable heartDrawable;
    private Drawable carDrawable;
    private Drawable wallDrawable;

    private FloatingActionButton leftButton, rightButton;

    public GamePanel(Context context, FloatingActionButton leftButton, FloatingActionButton rightButton) {
        super(context);
        this.context = context;
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        this.leftButton = leftButton;
        this.rightButton = rightButton;

        getHolder().addCallback(this);

        carDrawable = context.getResources().getDrawable(R.drawable.car);
        player = new Player(new Rect(0, 0,
                Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT), carDrawable);
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2, Constants.PLAYER_GAP);

        lives = new ArrayList<>();
        heartDrawable = context.getResources().getDrawable(R.drawable.heart);
        initLives();

        wallDrawable = context.getResources().getDrawable(R.drawable.wall);
        obstacleManager = new ObstacleManager(Constants.OBSTACLE_GAP,
                Constants.NUM_OF_ROADS,
                Constants.OBSTACLE_HEIGHT, wallDrawable);

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
        leftButton.setOnClickListener(v ->
                playerPoint.set(player.getRectangle().left, Constants.PLAYER_GAP));
        rightButton.setOnClickListener(v ->
                playerPoint.set(player.getRectangle().right, Constants.PLAYER_GAP));

        player.update(playerPoint);
        obstacleManager.update();

        if (obstacleManager.playerCollide(player)) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context, "Collision Accrued", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            vibrator.vibrate(500);
            lives.remove(lives.get(lives.size() - 1));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);

        player.customDraw(canvas);
        obstacleManager.customDraw(canvas);

        for (PlayerHealth life : lives) {
            life.customDraw(canvas);
        }
    }

    private void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

