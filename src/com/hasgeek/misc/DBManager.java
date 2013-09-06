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

    public static final String PROPOSALS_TABLE = "proposals";

    private static final String CREATE_TABLE_PROPOSALS = "CREATE TABLE " + PROPOSALS_TABLE + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "id TEXT NOT NULL UNIQUE, " +
            "title TEXT NOT NULL, " +
            "speaker TEXT NOT NULL, " +
            "section TEXT NOT NULL, " +
            "level TEXT NOT NULL, " +
            "description TEXT NOT NULL" +
            ");";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROPOSALS);
        Log.w(TAG, "Created proposals table.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Database needs an update from version " + String.valueOf(oldVersion) + " to " + String.valueOf(newVersion));
    }


}

