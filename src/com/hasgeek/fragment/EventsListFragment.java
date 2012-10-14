package com.hasgeek.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.hasgeek.EventsListLoader;
import com.hasgeek.R;


public class EventsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.item_eventslist,
                null,
                new String[] { "name" },
                new int[] { R.id.tv_name }
        );

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public void onResume() {
        super.onResume();
        View doh = View.inflate(getActivity(),R.layout.part_lv_xvii, null);
        LinearLayout xvii = (LinearLayout) doh.findViewById(R.id.ll_xvii);
        getListView().addFooterView(xvii, null, false);

        setListAdapter(mAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eventslist, container, false);
    }


    private class EventsListCursorAdapter extends SimpleCursorAdapter {
        public EventsListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
            super(context, layout, c, from, to);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return li.inflate(R.layout.item_eventslist, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);
        }

    }


    // Loader callbacks start here...

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.w("hsgk", "onCreateLoader");
        return new EventsListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        for (String s : cursor.getColumnNames()) {
            Log.w("hsgk", s);
        }
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

}
