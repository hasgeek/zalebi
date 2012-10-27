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

    public static final String EVENTS_TABLE = "events";
    public static final String SESSIONS_TABLE = "sessions";

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + EVENTS_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "hasgeekId INTEGER NOT NULL UNIQUE, " +
            "name TEXT NOT NULL, " +
            "location TEXT NOT NULL, " +
            "rootUrl TEXT NOT NULL, " +
            "lat FLOAT NOT NULL, " +
            "long FLOAT NOT NULL, " +
            "startDatetime TEXT NOT NULL, " +
            "endDatetime TEXT NOT NULL" +
            ");";

    private static final String CREATE_TABLE_SESSIONS = "CREATE TABLE " + SESSIONS_TABLE + " (" +
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

