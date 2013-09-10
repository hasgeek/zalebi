package com.hasgeek.misc;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;


public class DataProvider extends ContentProvider {

    private DBManager mDBM;
    private Context mContext;

    public static final String PROVIDER_NAME = "com.hasgeek.data";
    public static final String SQLITE_INSERT_OR_REPLACE_MODE = "__SQLITE_INSERT_OR_REPLACE_MODE__";

    public static final Uri PROPOSAL_URI = Uri.parse("content://" + PROVIDER_NAME + "/proposals");

    private static final UriMatcher uriMatcher;
    private static final int PROPOSALS_MATCH = 4201;
    private static final int PROPOSAL_MATCH = 4202;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "proposals", PROPOSALS_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "proposal/#", PROPOSAL_MATCH);
    }


    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDBM = DBManager.getInstance(mContext);
        return true;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PROPOSALS_MATCH:
                return "vnd.android.cursor.dir/vnd.com.hasgeek.proposals";
            case PROPOSAL_MATCH:
                return "vnd.android.cursor.item/vnd.com.hasgeek.proposals";
            default:
                throw new RuntimeException("Unsupported URI: " + uri);
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case PROPOSALS_MATCH:
                sqlBuilder.setTables(DBManager.PROPOSALS_TABLE);
                break;

            case PROPOSAL_MATCH:
                sqlBuilder.setTables(DBManager.PROPOSALS_TABLE);
                sqlBuilder.appendWhere(BaseColumns._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new RuntimeException("Incorrect URI matched!");
        }

        Cursor c = sqlBuilder.query(mDBM.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(mContext.getContentResolver(), uri);
        return c;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBM.getWritableDatabase();
        String id = uri.getPathSegments().get(1);
        int count;

        switch (uriMatcher.match(uri)) {
            case PROPOSALS_MATCH:
                count = db.delete(DBManager.PROPOSALS_TABLE, selection, selectionArgs);
                break;

            case PROPOSAL_MATCH:
                count = db.delete(
                        DBManager.PROPOSALS_TABLE,
                        BaseColumns._ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBM.getWritableDatabase();
        ContentValues v = new ContentValues(values);

        boolean replace = false;
        if (values.containsKey(SQLITE_INSERT_OR_REPLACE_MODE)) {
            replace = values.getAsBoolean(SQLITE_INSERT_OR_REPLACE_MODE);
            v.remove(SQLITE_INSERT_OR_REPLACE_MODE);
        }

        if (uri.getPathSegments().get(0).equals("proposals")) {
            try {
                long row;
                if (replace) {
                    row = db.replaceOrThrow(DBManager.PROPOSALS_TABLE, null, v);
                } else {
                    row = db.insertOrThrow(DBManager.PROPOSALS_TABLE, null, v);
                }
                if (row > 0) {
                    Uri u = ContentUris.withAppendedId(PROPOSAL_URI, row);
                    mContext.getContentResolver().notifyChange(u, null);
                    return u;
                }
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                throw new RuntimeException("Insertion failed.");
            }

        } else {
            throw new RuntimeException("Insert is broken.");
        }

        return null;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBM.getWritableDatabase();
        int count;

        switch (uriMatcher.match(uri)) {
            case PROPOSALS_MATCH:
                count = db.update(DBManager.PROPOSALS_TABLE, values, selection, selectionArgs);
                break;
            case PROPOSAL_MATCH:
                String id = uri.getPathSegments().get(1);
                count = db.update(DBManager.PROPOSALS_TABLE,
                        values,
                        BaseColumns._ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs
                );
                break;

            default:
                throw new RuntimeException("Unknown URI: " + uri);
        }

        mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }
}
