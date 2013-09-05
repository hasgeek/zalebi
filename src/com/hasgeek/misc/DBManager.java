package com.hasgeek.misc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "hsgk";
    public static final int DATABASE_VERSION = 1;
    public static final String TAG = "HasGeek";

    public static final String WORKSPACES_TABLE = "workspaces";
    public static final String PROFILES_TABLE = "profiles";

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + WORKSPACES_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "buid TEXT NOT NULL UNIQUE, " +
            "name TEXT NOT NULL, " +
            "title TEXT NOT NULL, " +
            "profile_id TEXT NOT NULL, " +
            "date_location TEXT NOT NULL, " +
            "start_date TEXT NOT NULL, " +
            "end_date TEXT NOT NULL, " +
            "data_api_url TEXT NOT NULL, " +
            "start_ts TEXT, " +
            "end_ts TEXT, " +
            "" +
            ");";

    private static final String CREATE_TABLE_SESSIONS = "CREATE TABLE " + PROFILES_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "foodId INTEGER NOT NULL, " +
            "foodImage TEXT NOT NULL" +
            ");";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EVENTS);
        Log.w(TAG, "Created events table.");

        db.execSQL(CREATE_TABLE_SESSIONS);
        Log.w(TAG, "Created sessions table.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Database needs an update from version " + String.valueOf(oldVersion) + " to " + String.valueOf(newVersion));
    }


}

