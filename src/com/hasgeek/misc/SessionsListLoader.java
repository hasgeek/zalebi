package com.hasgeek.misc;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.hasgeek.fragment.DaysListFragment;

import java.util.ArrayList;
import java.util.List;


public class SessionsListLoader extends AsyncTaskLoader<List<EventSession>> {

    private List<EventSession> mData;
    private int mMode;


    public SessionsListLoader(Context context, int mode) {
        super(context);
        mMode = mode;
    }


    @Override
    public List<EventSession> loadInBackground() {
        List<EventSession> esList = new ArrayList<EventSession>();
        Cursor sessions;
        if (mMode == DaysListFragment.All_SESSIONS) {
            sessions = getContext().getContentResolver().query(
                    DataProvider.PROPOSAL_URI,
                    new String[] { "id", "title", "speaker", "section", "level", "description", "url", "bookmarked" },
                    null,
                    null,
                    "id ASC"
            );
        } else {
            sessions = getContext().getContentResolver().query(
                    DataProvider.PROPOSAL_URI,
                    new String[] { "id", "title", "speaker", "section", "level", "description", "url", "bookmarked" },
                    "bookmarked = ?",
                    new String[] { "true" },
                    "id ASC"
            );
        }

        if (sessions.moveToFirst()) {
            do {
                boolean bookmarkState;
                if (sessions.isNull(sessions.getColumnIndex("bookmarked"))) {
                    bookmarkState = false;
                } else {
                    bookmarkState = sessions.getString(sessions.getColumnIndex("bookmarked")).equals("true");
                }
                EventSession es = new EventSession(
                        sessions.getString(sessions.getColumnIndex("id")),
                        sessions.getString(sessions.getColumnIndex("title")),
                        sessions.getString(sessions.getColumnIndex("speaker")),
                        sessions.getString(sessions.getColumnIndex("section")),
                        sessions.getString(sessions.getColumnIndex("level")),
                        sessions.getString(sessions.getColumnIndex("description")),
                        sessions.getString(sessions.getColumnIndex("url")),
                        bookmarkState
                );
                esList.add(es);

            } while (sessions.moveToNext());
        }
        sessions.close();
        return esList;
    }


    @Override
    public void deliverResult(List<EventSession> data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // The old data may still be in use (i.e. bound to an adapter, etc.), so
        // we must protect it until the new data has been delivered.
        List<EventSession> oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
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
            releaseResources(mData);
            mData = null;
        }
    }


    @Override
    public void onCanceled(List<EventSession> data) {
        super.onCanceled(data);
        releaseResources(data);
    }


    private void releaseResources(List<EventSession> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

}
