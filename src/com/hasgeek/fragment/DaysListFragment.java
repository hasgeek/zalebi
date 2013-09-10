package com.hasgeek.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hasgeek.R;
import com.hasgeek.activity.SessionDetailActivity;
import com.hasgeek.misc.EventSession;
import com.hasgeek.misc.SessionsListLoader;

import java.util.List;


public class DaysListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<EventSession>> {

    private static final int REQUEST_SESSION_DETAIL = 4201;
    private List<EventSession> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }


    @Override
    public Loader<List<EventSession>> onCreateLoader(int i, Bundle bundle) {
        return new SessionsListLoader(getActivity());
    }


    @Override
    public void onLoadFinished(Loader<List<EventSession>> listLoader, List<EventSession> eventSessions) {
        setListShown(true);
        mAdapter = eventSessions;
        setListAdapter(new SessionsListAdapter());
    }


    @Override
    public void onLoaderReset(Loader<List<EventSession>> listLoader) {
    }


    private class SessionsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mAdapter.size();
        }


        @Override
        public EventSession getItem(int i) {
            return mAdapter.get(i);
        }


        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row_session, viewGroup, false);
            }

            TextView title = (TextView) convertView.findViewById(R.id.tv_session_title);
            title.setText(mAdapter.get(position).getTitle());

            TextView speaker = (TextView) convertView.findViewById(R.id.tv_session_speaker);
            speaker.setText(mAdapter.get(position).getSpeaker());

            return convertView;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.w("ASD", "isBookmarked " + mAdapter.get(position).isBookmarked());
        Intent i = new Intent(getActivity(), SessionDetailActivity.class);
        i.putExtra("session", mAdapter.get(position));
        startActivityForResult(i, REQUEST_SESSION_DETAIL);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SESSION_DETAIL:
                getLoaderManager().restartLoader(0, null, this);
        }
    }
}
