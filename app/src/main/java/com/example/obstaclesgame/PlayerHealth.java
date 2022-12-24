package com.example.obstaclesgame;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.example.obstaclesgame.interfaces.IGameObject;

public class PlayerHealth implements IGameObject {

    private Rect rectangle;
    private Drawable drawable;

    public PlayerHealth(int x, int y, int width, int height, Drawable drawable) {
        this.drawable = drawable;
        rectangle = new Rect(x, y, x + width, y + height);
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
