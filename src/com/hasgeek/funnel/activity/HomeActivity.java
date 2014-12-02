package com.hasgeek.funnel.activity;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hasgeek.funnel.R;
import com.hasgeek.funnel.bus.BusProvider;
import com.hasgeek.funnel.bus.DroidconAPICalledEvent;
import com.hasgeek.funnel.fragment.EventsListFragment;
import com.hasgeek.funnel.service.APIService;
import com.squareup.otto.Subscribe;


public class HomeActivity extends Activity {

    private LinearLayout mLoading;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean first_time=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent i = new Intent(this, APIService.class);
        i.putExtra(APIService.MODE, APIService.SYNC_DROIDCON2013);
        startService(i);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if (sp.getBoolean("first_run", true)) {
            first_time=true;
            mLoading = (LinearLayout)findViewById(R.id.main_activity_progress);
            final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
            mLoading.setAnimation(animationFadeIn);
        }
        else {
            Intent ed = new Intent(getApplicationContext(), EventDetailActivity.class);
            ed.putExtra("name", getString(R.string.droidcon2013_title));
            ed.putExtra("dateString", getString(R.string.droidcon2013_date));
            startActivity(ed);
            finish();

        }

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
        if (first_time) {
            Intent ed = new Intent(getApplicationContext(), EventDetailActivity.class);
            ed.putExtra("name", getString(R.string.droidcon2013_title));
            ed.putExtra("dateString", getString(R.string.droidcon2013_date));
            startActivity(ed);
            finish();
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