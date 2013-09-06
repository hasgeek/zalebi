package com.hasgeek.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hasgeek.R;
import com.hasgeek.bus.BusProvider;
import com.hasgeek.bus.JSFooAPICalledEvent;
import com.hasgeek.fragment.EventsListFragment;
import com.hasgeek.service.APIService;
import com.squareup.otto.Subscribe;


public class HomeActivity extends Activity {

    public static final String TAG = "HasGeek";

    private ProgressDialog mBusy;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("first_run", true)) {
            mBusy = ProgressDialog.show(this, "Loading.", "Busy...");
        }

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_events);
        RelativeLayout loading = (RelativeLayout) findViewById(R.id.rl_loading);
        fl.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        EventsListFragment e = new EventsListFragment();
        ft.add(R.id.fl_events, e);
        ft.commit();

        Intent i = new Intent(this, APIService.class);
        i.putExtra(APIService.MODE, APIService.SYNC_JSFOO);
        startService(i);
    }


    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe
    public void jsfooAPICallDone(JSFooAPICalledEvent meh) {
        if (mBusy != null) {
            mBusy.dismiss();
        }
    }
}
