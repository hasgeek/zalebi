package com.hasgeek.zalebi.fragments.space;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.adapters.SessionsAdapter;
import com.hasgeek.zalebi.adapters.VenuesAdapter;
import com.hasgeek.zalebi.adapters.utils.DividerItemDecoration;
import com.hasgeek.zalebi.api.model.Section;
import com.hasgeek.zalebi.api.model.Session;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.loader.LoadSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SingleSpaceLoadedEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
public class ScheduleFragment extends Fragment {

    String LOG_TAG = "ScheduleFragment";
    RecyclerView mRecyclerView;
    private Bus mBus;
    private SwipeRefreshLayout swipeLayout;
    private SessionsAdapter mAdapter;
    private Space space;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        space = Parcels.unwrap(getArguments().getParcelable("space"));
        View v = inflater.inflate(R.layout.fragment_space_schedule, container, false);

        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.fragment_proposal_swipe_container);
        swipeLayout.setOnRefreshListener(mOnSwipeListener);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_space_schedule_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(scrollListener);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getBus().register(this);
        getBus().post(new APIRequestSingleSpaceEvent(space.getJsonUrl()));
        mBus.post(new LoadSingleSpaceEvent(space.getJsonUrl()));
    }

    @Override
    public void onPause() {
        super.onPause();
        getBus().unregister(this);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnSwipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(LOG_TAG, "onRefresh() POST LoadScheduleEvent");
            getBus().post(new APIRequestSingleSpaceEvent(space.getJsonUrl()));
        }
    };

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
            swipeLayout.setEnabled(topRowVerticalPosition >= 0);

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
    };

    @Subscribe
    public void onSingleSpaceLoaded(SingleSpaceLoadedEvent event) {
        Log.i(LOG_TAG, "onSingleSpaceLoaded() SUBSCRIPTION SpacesLoadedEvent");
        Collections.sort(event.getSessions(), new Comparator<Session>() {
            @Override
            public int compare(Session lhs, Session rhs) {

                DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                m_ISO8601Local.setTimeZone(TimeZone.getTimeZone("GMT+0"));

                try {
                    return m_ISO8601Local.parse(lhs.getStart()).compareTo(m_ISO8601Local.parse(rhs.getStart()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        mAdapter = new SessionsAdapter(getActivity(), event.getSessions());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    @Subscribe
    public void onAPIError(APIErrorEvent event) {
        Toast.makeText(getActivity(), "Network trouble?", Toast.LENGTH_SHORT).show();
        swipeLayout.setRefreshing(false);
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }
}
