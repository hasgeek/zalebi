package com.hasgeek.zalebi.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.AuthService;
import com.hasgeek.zalebi.api.ContactExchangeService;
import com.hasgeek.zalebi.api.model.Attendee;
import com.hasgeek.zalebi.api.model.ExchangeContact;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncAttendeesEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncContactsEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSyncContactsEvent;
import com.hasgeek.zalebi.eventbus.event.loader.LoadSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SingleSpaceLoadedEvent;
import com.hasgeek.zalebi.fragments.space.ScheduleFragment;
import com.hasgeek.zalebi.fragments.space.ScrollbackFragment;
import com.hasgeek.zalebi.fragments.space.contactexchange.AttendeeFragment;
import com.hasgeek.zalebi.fragments.space.contactexchange.ContactFragment;
import com.hasgeek.zalebi.fragments.space.contactexchange.ScannerFragment;
import com.hasgeek.zalebi.providers.VCFProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;

public class SingleSpaceActivity extends ActionBarActivity {

    private final String LOG_TAG = "SingleSpaceActivity";
    SpacePagerAdapter pageAdapter;
    ViewPager pager;

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
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
        if (savedInstanceState != null) {
            spaceBundle = savedInstanceState.getBundle("state");
            space = Parcels.unwrap(spaceBundle.getParcelable("space"));

            getSupportActionBar().setTitle(space.getTitle());
            getSupportActionBar().setSubtitle(space.getDatelocation());
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        List<Fragment> fragments = getFragments();
        pageAdapter = new SpacePagerAdapter(getSupportFragmentManager(), fragments);


        pager =
                (ViewPager) findViewById(R.id.activity_single_space_viewpager);
        pager.setAdapter(pageAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_single_space_viewpager_tabstrip);
        tabs.setViewPager(pager);
        mBus=getBus();
        mBus.register(this);
        mBus.post(new APIRequestSyncAttendeesEvent(space.getId(), space.getUrl()));
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
        this.invalidateOptionsMenu();
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

        ContactFragment contactFragment = new ContactFragment();
        contactFragment.setArguments(spaceBundle);
        fList.add(contactFragment);

        fList.add(new ScrollbackFragment());

//        AttendeeFragment attendeeFragment = new AttendeeFragment();
//        attendeeFragment.setArguments(spaceBundle);
//        fList.add(attendeeFragment);

//        ScannerFragment scan = new ScannerFragment();
//        scan.setArguments(spaceBundle);
//        fList.add(scan);

        //ScrollbackFragment chat = new ScrollbackFragment();




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
    public boolean onPrepareOptionsMenu(Menu menu) {

        if(!AuthService.isLoggedIn()) {
            menu.findItem(R.id.action_scan).setVisible(false);
            menu.findItem(R.id.action_export).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        else {
            menu.findItem(R.id.action_login).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void updatePages() {
        int i = pager.getCurrentItem();
        pager.setAdapter(pageAdapter);
        pager.setCurrentItem(i);
        mBus.post(new APIRequestSyncContactsEvent(space.getId(), space.getUrl()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scan) {
            FragmentManager fm = getSupportFragmentManager();
            ScannerFragment scannerFragment = new ScannerFragment();
            scannerFragment.setArguments(spaceBundle);
            scannerFragment.show(fm, "scanner_fragment");

            return true;
        }

        else if(id == R.id.action_refresh) {
            updatePages();
        }

        else if (id == R.id.action_export) {
            Collection<VCard> exportContactList = ContactExchangeService.getVCardsFromExchangeContacts();
            if(!exportContactList.isEmpty()) {
                try {
                    File cacheDir = getCacheDir();
                    File file = new File(cacheDir, "contacts.vcf");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    Ezvcard.write(exportContactList).go(file);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("*/*");
                    //Add the attachment by specifying a reference to our custom ContentProvider
                    //and the specific file of interest
                    shareIntent.putExtra(
                            Intent.EXTRA_STREAM,
                            Uri.parse("content://" + VCFProvider.AUTHORITY + "/"
                                    + "contacts.vcf"));
                    startActivity(Intent.createChooser(shareIntent, "Export contacts"));

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(SingleSpaceActivity.this, "No contacts to export", Toast.LENGTH_SHORT).show();
            }
        }

        else if(id == R.id.action_login) {
            String url = "http://auth.hasgeek.com/auth?client_id=eDnmYKApSSOCXonBXtyoDQ&scope=id+email+phone+organizations+teams+com.talkfunnel:*&response_type=token";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }

        else if(id == R.id.action_logout) {
            AuthService.deleteUserToken();
            this.invalidateOptionsMenu();
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
                    return "Contacts";
                case 2:
                    return "Chat";
                case 3:
                    return "Scan";
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
