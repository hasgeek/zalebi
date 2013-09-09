package com.hasgeek.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.hasgeek.R;
import com.hasgeek.fragment.DaysListFragment;


public class EventDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getIntent().getStringExtra("name"));
        ab.setSubtitle(getIntent().getStringExtra("dateString"));

        setContentView(R.layout.activity_eventdetail);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DaysListFragment dlf = new DaysListFragment();
        ft.add(R.id.fl_fragment, dlf);
        ft.commit();
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

}
