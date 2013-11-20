package com.hasgeek.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.hasgeek.R;
import com.hasgeek.activity.EventDetailActivity;
import com.hasgeek.misc.EventsListLoader;

import java.util.ArrayList;
import java.util.List;


public class EventsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mAdapter;

    private static String TAG = "hsgk";
    private String mDateString;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new TempEventListAdapter());

        //todo move to loader once there are > 1 events
//        mAdapter = new EventsListCursorAdapter(
//                getActivity(),
//                R.layout.item_eventslist,
//                null,
//                new String[]{"name"},
//                new int[]{R.id.tv_name}
//        );
//
//        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_eventslist, container, false);
    }


    private class TempEventListAdapter extends BaseAdapter {

        List<String> data;

        public TempEventListAdapter() {
            data = new ArrayList<String>();
            data.add(getString(R.string.droidcon2013_title));
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_eventslist, viewGroup, false);
            }

            TextView eventName = (TextView) convertView.findViewById(R.id.tv_event_name);
            eventName.setText(data.get(position));

            TextView eventDate = (TextView) convertView.findViewById(R.id.tv_event_date);
            eventDate.setText(R.string.droidcon2013_date);

            return convertView;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

//        Cursor c = mAdapter.getCursor();
//        c.moveToPosition(position);

        Intent ed = new Intent(getActivity(), EventDetailActivity.class);
        ed.putExtra("name", getString(R.string.droidcon2013_title));
        ed.putExtra("dateString", getString(R.string.droidcon2013_date));
        startActivity(ed);
    }


//    private class EventsListCursorAdapter extends SimpleCursorAdapter {
//        public EventsListCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
//            super(context, layout, c, from, to);
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            return li.inflate(R.layout.item_eventslist, parent, false);
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            super.bindView(view, context, cursor);
//
//            TextView daterange = (TextView) view.findViewById(R.id.tv_date);
//            String from = cursor.getString(cursor.getColumnIndex("startDatetime"));
//            String to = cursor.getString(cursor.getColumnIndex("endDatetime"));
//
//            SimpleDateFormat theirFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            SimpleDateFormat ourFormat = new SimpleDateFormat("d MMMM yyyy");
//            SimpleDateFormat sameMonthCheck = new SimpleDateFormat("MMMM yyyy");
//
//            try {
//                if (ourFormat.format(theirFormat.parse(from)).equals(ourFormat.format(theirFormat.parse(to)))) {
//                    // single day event
//                    mDateString = ourFormat.format(theirFormat.parse(from));
//                    daterange.setText(mDateString);
//
//                } else if (sameMonthCheck.format(theirFormat.parse(from)).equals(sameMonthCheck.format(theirFormat.parse(to)))) {
//                    // month and year are same
//                    String dateonly = new SimpleDateFormat("d").format(theirFormat.parse(from));
//                    mDateString = dateonly + " & " + ourFormat.format(theirFormat.parse(to));
//                    daterange.setText(mDateString);
//                }
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }


    // Loader callbacks start here...

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new EventsListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        View doh = View.inflate(getActivity(), R.layout.part_lv_xvii, null);
        TextView xvii = (TextView) doh.findViewById(R.id.tv_xvii);
        getListView().addFooterView(xvii, null, false);

        setListAdapter(mAdapter);

        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

}
