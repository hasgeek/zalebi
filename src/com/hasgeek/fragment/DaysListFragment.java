package com.hasgeek.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hasgeek.R;
import com.hasgeek.misc.EventSession;
import com.hasgeek.misc.SessionsListLoader;

import java.util.List;


public class DaysListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<EventSession>> {

    private List<EventSession> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);
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
}
