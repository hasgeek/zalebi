package com.hasgeek.zalebi;

import android.app.Application;
import android.util.Log;

import com.hasgeek.zalebi.api.API;
import com.hasgeek.zalebi.api.ContactExchangeService;
import com.hasgeek.zalebi.api.SpacesService;
import com.hasgeek.zalebi.eventbus.BusProvider;
import com.hasgeek.zalebi.eventbus.event.api.APIErrorEvent;
import com.orm.SugarApp;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by karthik on 23-12-2014.
 */
public class Talkfunnel extends SugarApp {

    private Bus mBus = BusProvider.getInstance();
    private SpacesService mSpacesService;
    private ContactExchangeService mContactExchangeService;
    private API api;

    @Override
    public void onCreate() {
        super.onCreate();

        mSpacesService = new SpacesService(mBus, getApplicationContext());
        mContactExchangeService = new ContactExchangeService(mBus, getApplicationContext());

        mBus.register(mSpacesService);
        mBus.register(mContactExchangeService);

        api = new API(mBus, getApplicationContext());
        mBus.register(api);

        mBus.register(this); //listen for "global" events
    }

    @Subscribe
    public void onApiError(APIErrorEvent event) {
        Log.e("TalkfunnelApp", event.getMessage()+"");
    }

}
