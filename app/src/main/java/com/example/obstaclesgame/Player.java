package com.example.obstaclesgame;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Player implements GameObject{

    private Rect rectangle;
    private Drawable drawable;

    public Player(Rect rectangle, Drawable drawable) {
        this.rectangle = rectangle;
        this.drawable = drawable;
    }

    @Override
    public void customDraw(Canvas canvas) {
        drawable.setBounds(rectangle);
        drawable.draw(canvas);
    }

    @Override
    public void update() {
    }

    public void update(Point point) {
        int rWidth = rectangle.width();
        int rHeight = rectangle.height();

        int left = Math.min(Math.max(point.x - rWidth/2,0),Constants.SCREEN_WIDTH - rWidth);
        int top = Math.min(Math.max(point.y - rHeight/2,0), Constants.SCREEN_HEIGHT - rHeight);
        int right = left + rWidth;
        int bottom = top + rHeight;

        rectangle.set(left, top, right, bottom);
    }

    public Rect getRectangle() {
        return this.rectangle;
    }
}
