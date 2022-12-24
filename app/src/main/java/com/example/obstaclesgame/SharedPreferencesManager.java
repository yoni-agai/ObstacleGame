package com.example.obstaclesgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class SharedPreferencesManager {

    private static final String DB_FILE = "DB_FILE";
    private static final String SP_LEADERBOARDS = "SP_LEADERBOARDS";
    private static final String SP_HIGHEST_SCORE = "SP_HIGHEST_SCORE";
    private static final String SP_LOWEST_SCORE = "SP_LOWEST_SCORE";
    private static final String SP_PLAYER_COUNT = "SP_PLAYER_COUNT";

    public static void putLong(Context context, String key, long value) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    private static long getLong(Context context, String key, long def) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        return preferences.getLong(key, def);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static int getInt(Context context, String key, int def) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        return preferences.getInt(key, def);
    }

    private static void putString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getString(Context context, String key, String def) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        return preferences.getString(key, def);
    }

    public static void saveNewLeaderBoards(Context context, String json) {
        putString(context, SP_LEADERBOARDS, json);
    }

    public static ArrayList<HighScore> getLeaderBoards(Context context) {
        String json = getString(context, SP_LEADERBOARDS, "");
        ArrayList<HighScore> leaderboard = new Gson().fromJson(json, new TypeToken<ArrayList<HighScore>>() {}.getType());
        if(leaderboard == null) {
            leaderboard = new ArrayList<HighScore>();
        }
        return leaderboard;
    }

    public static void saveNewHighestScore(Context context, long score) {
        putLong(context, SP_HIGHEST_SCORE, score);
    }

    public static long getHighestScore(Context context) {
        return getLong(context, SP_HIGHEST_SCORE, 0);
    }

    public static void saveNewLowestScore(Context context, long score) {
        putLong(context, SP_LOWEST_SCORE, score);
    }

    public static long getLowestScore(Context context) {
        return getLong(context, SP_LOWEST_SCORE, 0);
    }

    public static void savePlayerCount(Context context, int count) {
        putInt(context, SP_PLAYER_COUNT, count);
    }

    public static int getPlayerCount(Context context) {
        return getInt(context, SP_PLAYER_COUNT, 0);
    }
}
