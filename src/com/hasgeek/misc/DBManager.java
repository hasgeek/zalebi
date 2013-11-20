package com.hasgeek.misc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "hsgk";
    public static final int DATABASE_VERSION = 1;

    public static final String SESSIONS_TABLE = "sessions";

    private static DBManager mInstance = null;

    private static final String CREATE_TABLE_SESSIONS = "CREATE TABLE " + SESSIONS_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id TEXT NOT NULL UNIQUE, " +
            "title TEXT NOT NULL, " +
            "speaker TEXT NOT NULL, " +
            "section TEXT NOT NULL, " +
            "level TEXT NOT NULL, " +
            "description TEXT NOT NULL, " +
            "url TEXT NOT NULL, " +
            "bookmarked BOOLEAN" +
            ");";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static DBManager getInstance(Context c) {
        if (mInstance == null) {
            mInstance = new DBManager(c.getApplicationContext());
        }

        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SESSIONS);
        Log.w("Hasgeek", "Created sessions table.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("Hasgeek", "Database needs an update from version " + String.valueOf(oldVersion) + " to " + String.valueOf(newVersion));
    }
}

