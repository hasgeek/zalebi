package com.hasgeek.zalebi.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hasgeek.zalebi.R;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.fragments.space.ProposalFragment;
import com.hasgeek.zalebi.fragments.space.SectionFragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SingleSpaceActivity extends ActionBarActivity {

    private final String LOG_TAG = "SingleSpaceActivity";
    SpacePagerAdapter pageAdapter;
    Space space;
    Bundle spaceBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_space);
        if (getIntent().getExtras() != null) {
            spaceBundle = getIntent().getExtras().getBundle("bundle");
            space = Parcels.unwrap(spaceBundle.getParcelable("space"));

            getSupportActionBar().setTitle(space.getTitle());
            getSupportActionBar().setSubtitle(space.getDatelocation());
        } else if (savedInstanceState != null) {
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
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        ProposalFragment p = new ProposalFragment();
        p.setArguments(spaceBundle);

        SectionFragment s = new SectionFragment();
        s.setArguments(spaceBundle);

        fList.add(s);
        fList.add(p);


        return fList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState != null) {
            outState.putBundle("state", spaceBundle);
        }
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
                    return "Sections";
                case 1:
                    return "Proposals";
            }
            return null;
        }
    }
}
