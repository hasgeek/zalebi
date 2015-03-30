package com.hasgeek.zalebi.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.adapters.SectionsAdapter;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.loader.LoadSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SingleSpaceLoadedEvent;
import com.hasgeek.zalebi.fragments.space.ProposalFragment;
import com.hasgeek.zalebi.fragments.space.RoomFragment;
import com.hasgeek.zalebi.fragments.space.ScannerFragment;
import com.hasgeek.zalebi.fragments.space.ScheduleFragment;
import com.hasgeek.zalebi.fragments.space.SectionFragment;
import com.hasgeek.zalebi.fragments.space.VenueFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SingleSpaceActivity extends ActionBarActivity {

    private final String LOG_TAG = "SingleSpaceActivity";
    SpacePagerAdapter pageAdapter;
    Space space;
    Bundle spaceBundle;
    Bus mBus;

    public SingleSpaceLoadedEvent spaceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_space);
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                spaceBundle = getIntent().getExtras().getBundle("bundle");
                space = Parcels.unwrap(spaceBundle.getParcelable("space"));

                getSupportActionBar().setTitle(space.getTitle());
                getSupportActionBar().setSubtitle(space.getDatelocation());
            }
        }
        if (savedInstanceState != null) {
            spaceBundle = savedInstanceState.getBundle("state");
            space = Parcels.unwrap(spaceBundle.getParcelable("space"));

            getSupportActionBar().setTitle(space.getTitle());
            getSupportActionBar().setSubtitle(space.getDatelocation());
        }

        List<Fragment> fragments = getFragments();
        pageAdapter = new SpacePagerAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager =
                (ViewPager) findViewById(R.id.activity_single_space_viewpager);
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(1);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_single_space_viewpager_tabstrip);
        tabs.setViewPager(pager);
        mBus=getBus();
    }

    @Subscribe
    public void onSingleSpaceLoaded(SingleSpaceLoadedEvent event) {
        Log.i(LOG_TAG, "onSingleSpaceLoaded() SUBSCRIPTION SpacesLoadedEvent");
        spaceData=event;
        pageAdapter.notifyDataSetChanged();
    }



    @Override
    protected void onResume() {
        super.onResume();
        mBus.register(this);
        mBus.post(new LoadSingleSpaceEvent(space.getJsonUrl()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

//        SectionFragment s = new SectionFragment();
//        s.setArguments(spaceBundle);
//        fList.add(s);
//
//        ProposalFragment p = new ProposalFragment();
//        p.setArguments(spaceBundle);
//        fList.add(p);
//
//        VenueFragment v = new VenueFragment();
//        v.setArguments(spaceBundle);
//        fList.add(v);
//
//        RoomFragment r = new RoomFragment();
//        r.setArguments(spaceBundle);
//        fList.add(r);

        ScheduleFragment sched = new ScheduleFragment();
        sched.setArguments(spaceBundle);
        fList.add(sched);

        //ScrollbackFragment chat = new ScrollbackFragment();

        //ScannerFragment scan = new ScannerFragment();
        //fList.add(scan);


        return fList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBundle("state", spaceBundle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_single_space_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SpacePagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public SpacePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Schedule";
                case 1:
                    return "Chat";
                case 2:
                    return "Scan";
//                case 3:
//                    return "Rooms";
//                case 4:
//                    return "Scanner";
            }
            return null;
        }
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }
}
