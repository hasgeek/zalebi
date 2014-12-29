package com.hasgeek.zalebi;

import android.app.Application;
import android.util.Log;

import com.hasgeek.zalebi.api.API;
import com.hasgeek.zalebi.api.SpacesService;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.APIErrorEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by karthik on 23-12-2014.
 */
public class Talkfunnel extends Application {

    private Bus mBus = BusProvider.getInstance();
    private SpacesService mSpacesService;
    private API api;

    @Override
    public void onCreate() {
        super.onCreate();

        mSpacesService = new SpacesService(mBus, getApplicationContext());
        mBus.register(mSpacesService);

        api.init(mBus, getApplicationContext());
        mBus.register(api);

        mBus.register(this); //listen for "global" events
    }

    @Subscribe
    public void onApiError(APIErrorEvent event) {
        Log.e("TalkfunnelApp", event.getMessage());
    }

}
