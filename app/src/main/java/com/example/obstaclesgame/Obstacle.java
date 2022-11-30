package com.example.obstaclesgame;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Obstacle implements GameObject {

    private Rect rectangle;
    private Drawable drawable;

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
    }

    public Obstacle(int x, int y, int width, int height, Drawable drawable) {
        this.drawable = drawable;
        rectangle = new Rect(x, y, x + width, y + height);
    }

    public boolean playerCollide(Player player) {
        return Rect.intersects(rectangle, player.getRectangle());
    }

    @Override
    public void customDraw(Canvas canvas) {
        drawable.setBounds(rectangle);
        drawable.draw(canvas);
    }

    @Override
    public void update() {
    }
}
