package com.hasgeek.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.hasgeek.R;
import com.hasgeek.fragment.DaysListFragment;
import com.hasgeek.fragment.ExploreEventFragment;


public class EventDetailActivity extends Activity implements ActionBar.TabListener {

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetail);

        EventSectionsPagerAdapter pagerAdapter = new EventSectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
        });

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getIntent().getStringExtra("name"));
        ab.setSubtitle(getIntent().getStringExtra("dateString"));

        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.addTab(ab.newTab().setText(R.string.schedule).setTabListener(this));
        ab.addTab(ab.newTab().setText(R.string.explore).setTabListener(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_eventdetail, menu);

        if (getActionBar().getSelectedTab().getPosition() == 0) {
            menu.findItem(R.id.action_toggle_show_bookmarks).setVisible(true);
        } else {
            menu.findItem(R.id.action_toggle_show_bookmarks).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentAct = new Intent(this, HomeActivity.class);
                parentAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(parentAct);
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
        invalidateOptionsMenu();
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    /**
     * Adapter for the schedule and explore tabs on the event detail page
     */
    private static class EventSectionsPagerAdapter extends FragmentPagerAdapter {

        public EventSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new DaysListFragment();

                case 1:
                    return new ExploreEventFragment();

                default:
                    throw new RuntimeException("Which fragment are we trying to load here?");
            }
        }


        @Override
        public int getCount() {
            return 2;
        }
    }
}
