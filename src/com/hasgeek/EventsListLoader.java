package com.hasgeek;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class EventsListLoader extends AsyncTaskLoader<Cursor> {

    DBManager mDBM;
    SQLiteDatabase mDatabase;
    Cursor mData;

    public EventsListLoader(Context context) {
        super(context);
        mDBM = new DBManager(context);
        mDatabase = mDBM.getWritableDatabase();
    }


    @Override
    public Cursor loadInBackground() {
        return mDatabase.query(
                DBManager.EVENTS_TABLE,
                new String[] { "_id", "hasgeekId", "name", "rootUrl", "startDatetime", "endDatetime" },
                null,
                null,
                null,
                null,
                null
        );
    }


    @Override
    public void deliverResult(Cursor data) {
        if (isReset()) {
            onReleaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // The old data may still be in use (i.e. bound to an adapter, etc.), so
        // we must protect it until the new data has been delivered.
        Cursor oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            onReleaseResources(oldData);
        }
    }


    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }


    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is; Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }


    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }


    @Override
    public void onCanceled(Cursor data) {
        super.onCanceled(data);
        onReleaseResources(data);
    }


    protected void onReleaseResources(Cursor data) {
        data.close();
        mDatabase.close();
    }

}

