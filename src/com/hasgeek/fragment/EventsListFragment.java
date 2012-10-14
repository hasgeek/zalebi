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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.hasgeek.EventsListLoader;
import com.hasgeek.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class EventsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static String TAG = "hsgk";
    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new EventsListCursorAdapter(
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
        View doh = View.inflate(getActivity(), R.layout.part_lv_xvii, null);
        TextView xvii = (TextView) doh.findViewById(R.id.tv_xvii);
        getListView().addFooterView(xvii, null, false);

        setListAdapter(mAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eventslist, container, false);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

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

            TextView daterange = (TextView) view.findViewById(R.id.tv_date);
            String from = cursor.getString(cursor.getColumnIndex("startDatetime"));
            String to = cursor.getString(cursor.getColumnIndex("endDatetime"));

            SimpleDateFormat theirFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat ourFormat = new SimpleDateFormat("d MMMM yyyy");
            SimpleDateFormat sameMonthCheck = new SimpleDateFormat("MMMM yyyy");

            try {
                if (ourFormat.format(theirFormat.parse(from)).equals(ourFormat.format(theirFormat.parse(to)))) {
                    // single day event
                    daterange.setText(ourFormat.format(theirFormat.parse(from)));

                } else if (sameMonthCheck.format(theirFormat.parse(from)).equals(sameMonthCheck.format(theirFormat.parse(to)))) {
                    // month and year are same
                    String dateonly = new SimpleDateFormat("d").format(theirFormat.parse(from));
                    daterange.setText(dateonly + " & " + ourFormat.format(theirFormat.parse(to)));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

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
