package com.hasgeek.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.hasgeek.R;
import com.hasgeek.bus.BusProvider;
import com.hasgeek.bus.DroidconAPICalledEvent;
import com.hasgeek.fragment.EventsListFragment;
import com.hasgeek.service.APIService;
import com.squareup.otto.Subscribe;


public class HomeActivity extends Activity {

    private ProgressDialog mBusy;
    private final Handler mHandler = new Handler(Looper.getMainLooper());


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
        i.putExtra(APIService.MODE, APIService.SYNC_DROIDCON2013);
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
    public void sillyHardcodedAPICallDone(DroidconAPICalledEvent meh) {
        if (mBusy != null) {
            mBusy.dismiss();
        }

        if (meh.hasMessage()) {
            final DroidconAPICalledEvent neh = meh;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setMessage(neh.getMessage())
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            });
        }
    }
}
