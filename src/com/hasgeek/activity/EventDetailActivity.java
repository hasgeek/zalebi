package com.hasgeek.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.hasgeek.R;


public class EventDetailActivity extends Activity implements ActionBar.TabListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetail);

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getIntent().getStringExtra("name"));
        ab.setSubtitle(getIntent().getStringExtra("dateString"));

        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.addTab(ab.newTab().setText(R.string.schedule).setTabListener(this));
        ab.addTab(ab.newTab().setText(R.string.explore).setTabListener(this));
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
        switch (tab.getPosition()) {
            case 0: // SCHEDULE
                break;

            case 1: // EXPLORE
                break;

        }
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
