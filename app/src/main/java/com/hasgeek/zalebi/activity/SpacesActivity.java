package com.hasgeek.zalebi.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.adapters.SpacesAdapter;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.loader.LoadSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SpacesLoadedEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class SpacesActivity extends ActionBarActivity {

    String LOG_TAG = "SpacesActivity";
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView mRecyclerView;
    private SpacesAdapter mAdapter;
    private Bus mBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(mOnSwipeListener);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.spaces_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(scrollListener);

    }

    @Override
    public void onResume() {
        super.onResume();

        swipeLayout.setRefreshing(true);
        Log.i(LOG_TAG, "onResume() POST LoadSpacesEvent");
        getBus().register(this);
        getBus().post(new LoadSpacesEvent("start"));
    }

    @Override
    public void onPause() {
        super.onPause();

        getBus().unregister(this);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnSwipeListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Log.i(LOG_TAG, "onRefresh() POST LoadSpacesEvent");
            getBus().post(new APIRequestSpacesEvent("spaces"));
        }
    };

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }

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
    public void onSpacesLoaded(SpacesLoadedEvent event) {
        Log.i(LOG_TAG, "onSpacesLoaded() SUBSCRIPTION SpacesLoadedEvent");
        mAdapter = new SpacesAdapter(this, event.getSpaces());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    @Subscribe
    public void onAPIError(APIErrorEvent event) {
        Toast.makeText(getApplicationContext(), "Network trouble? Are you logged in?", Toast.LENGTH_SHORT).show();
        swipeLayout.setRefreshing(false);
    }
}
