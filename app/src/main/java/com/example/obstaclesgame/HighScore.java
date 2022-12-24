package com.example.obstaclesgame;

public class HighScore {

    private double latitude;
    private double longitude;
    private long score;

    public HighScore(double latitude, double longitude, long score) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
