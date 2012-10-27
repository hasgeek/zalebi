package com.hasgeek.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.*;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.hasgeek.R;
import com.hasgeek.fragment.EventsListFragment;
import com.hasgeek.service.APIService;


public class HomeActivity extends Activity {

    public static final String TAG = "HasGeek";

    private SharedPreferences mSharedPrefs;
    private APIReceiver mReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        FrameLayout fl = (FrameLayout) findViewById(R.id.fl_events);
        RelativeLayout loading = (RelativeLayout) findViewById(R.id.rl_loading);

        if (mSharedPrefs.contains("last_sync_time")) {
            fl.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            EventsListFragment e = new EventsListFragment();
            ft.add(R.id.fl_events, e);
            ft.commit();

        } else {
            Intent i = new Intent(this, APIService.class);
            i.putExtra(APIService.MODE, APIService.SYNC_EVERYTHING);
            startService(i);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter inf = new IntentFilter();
        inf.addAction(APIService.SYNC_EVERYTHING_DONE);
        mReceiver = new APIReceiver();
        registerReceiver(mReceiver, inf);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }


    private class APIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(APIService.SYNC_EVERYTHING_DONE)) {
                Log.w(TAG, "things are done, lets show frag");
                FrameLayout fl = (FrameLayout) findViewById(R.id.fl_events);
                fl.setVisibility(View.VISIBLE);
                RelativeLayout loading = (RelativeLayout) findViewById(R.id.rl_loading);
                loading.setVisibility(View.GONE);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                EventsListFragment e = new EventsListFragment();
                ft.add(R.id.fl_events, e);
                ft.commit();
            }
        }
    }

}
