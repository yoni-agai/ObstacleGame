package com.example.obstaclesgame;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.obstaclesgame.interfaces.IGameObject;

public class ObstacleManager implements IGameObject {

    private Drawable wall;
    private Drawable coin;

    private ArrayList<Obstacle> obstacles;
    private int[] xStartOptions;

    private int num_of_roads;
    private int road_width;
    private int obstacle_height;
    private int obstacleGap;

    private long startTime;

    private boolean isWall;
    private boolean isCollisionDetected;
    private boolean isWallCollision;



    public ObstacleManager (int obstacleGap, int num_of_roads, int obstacle_height, Drawable wall, Drawable coin) {
        this.wall = wall;
        this.coin = coin;

        this.obstacleGap = obstacleGap;
        this.num_of_roads = num_of_roads;
        this.obstacle_height = obstacle_height;
        this.road_width = Constants.SCREEN_WIDTH/num_of_roads;

        this.xStartOptions = populateStartPoints();
        this.obstacles = new ArrayList<>();

        this.startTime = System.currentTimeMillis();

        this.isWall = true;
        this.isCollisionDetected = false;
        this.isWallCollision = false;

        populateObstacles();
    }

    private int[] populateStartPoints() {
        int[] startPointsOptions = new int[num_of_roads];

        for(int i = 0; i < startPointsOptions.length; i++) {
            startPointsOptions[i] = i * road_width;
        }
        return startPointsOptions;
    }

    private void populateObstacles() {
        int currY = -Constants.SCREEN_HEIGHT;

        while(currY < 0) {
            int randX = xStartOptions[new Random().nextInt(xStartOptions.length)];

            if (isWall) {
                obstacles.add(new Obstacle(randX, currY, road_width, obstacle_height, isWall, wall));
                isWall = false;

            }
            else {
                obstacles.add(new Obstacle(randX, currY, road_width, obstacle_height, isWall, coin));
                isWall = true;
            }
            currY += obstacle_height + obstacleGap;
        }
    }

    public boolean playerCollide(Player player) {
        for (Obstacle ob : obstacles) {
            if (ob.playerCollide(player)) {
                isWallCollision = ob.isWall();
                isCollisionDetected = true;
                obstacles.remove(ob);
                return true;
            }
        }
        return false;
    }

    @Override
    public void customDraw(Canvas canvas) {
        for (Obstacle ob : obstacles) {
           ob.customDraw(canvas);
        }
    }

    @Override
    public void update() {
        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();

        int speedOverTime = (int)(Math.floor(Constants.SPEED * elapsedTime));

        boolean isExitScreen = false;

        for (Obstacle ob : obstacles) {
            ob.incrementY(speedOverTime);
            if (obstacles.get(obstacles.size()-1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
                isExitScreen = true;
            }
        }

        if(isExitScreen) {
            int randX = xStartOptions[new Random().nextInt(xStartOptions.length)];

            boolean isObstacleIsWall = obstacles.get(obstacles.size() -1 ).isWall();
            if (isObstacleIsWall) {
                obstacles.add(0, new Obstacle(randX, -obstacle_height, road_width, obstacle_height, isObstacleIsWall, wall));
            }
            else {
                obstacles.add(0, new Obstacle(randX, -obstacle_height, road_width, obstacle_height, isObstacleIsWall, coin));
            }
            obstacles.remove(obstacles.size() - 1);
        }

        if (isCollisionDetected) {
            int randX = xStartOptions[new Random().nextInt(xStartOptions.length)];
            if (isWallCollision) {
                obstacles.add(0, new Obstacle(randX, -Constants.SCREEN_HEIGHT/2, road_width, obstacle_height, isWallCollision, wall));
            }
            else {
                obstacles.add(0, new Obstacle(randX, -Constants.SCREEN_HEIGHT/2, road_width, obstacle_height, isWallCollision, coin));
            }

            isCollisionDetected = false;
        }
    }

    public boolean isWallCollision() {
        return isWallCollision;
    }
}
