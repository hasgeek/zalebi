package com.hasgeek.zalebi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SingleSpaceLoadedEvent;
import com.hasgeek.zalebi.fragments.space.contactexchange.ContactFragment;
import com.hasgeek.zalebi.fragments.space.contactexchange.ScannerFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
public class ContactExchangeActivity extends ActionBarActivity {

    private final String LOG_TAG = "ContactExchangeActivity";

    private Bus mBus;
    ContactExchangeAdapter pageAdapter;
    ViewPager pager;
    Space space;
    Bundle spaceBundle;
    int proposal_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);
        spaceBundle = getIntent().getExtras().getBundle("bundle");
        proposal_index = getIntent().getExtras().getInt("proposal_index");
        space = Parcels.unwrap(spaceBundle.getParcelable("space"));

        getSupportActionBar().setTitle("Contact Exchange");
        getSupportActionBar().setSubtitle(space.getTitle());

        pageAdapter = new ContactExchangeAdapter(getSupportFragmentManager(), getFragments());
        ViewPager pager =
                (ViewPager) findViewById(R.id.activity_contactexchange_viewpager);
        pager.setAdapter(pageAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_contactexchange_viewpager_tabstrip);
        tabs.setViewPager(pager);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(new ScannerFragment());

        fList.add(new ContactFragment());

        return fList;
    }


    @Override
    public void onResume() {
        super.onResume();
        getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getBus().unregister(this);
    }

    @Subscribe
    public void onSingleSpaceLoaded(SingleSpaceLoadedEvent event) {
        Log.i(LOG_TAG, "onSpacesLoaded() SUBSCRIPTION SpacesLoadedEvent");
    }

    @Subscribe
    public void onAPIError(APIErrorEvent event) {
        Toast.makeText(this, "Network trouble? Are you logged in?", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_proposal, menu);
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

    class ContactExchangeAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public ContactExchangeAdapter(FragmentManager fm, List<Fragment> fragments) {
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
                    return "Scan";
                case 1:
                    return "Contacts";
            }
            return "";
        }


    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }
}
