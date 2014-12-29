package com.hasgeek.zalebi.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.event.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.APIRequestSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.APIResponseSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.LoadSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.SpacesLoadedEvent;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by karthik on 24-12-2014.
 */
public class SpacesService {

    String LOG_TAG = "SpacesService";
    private Bus mBus;
    Context ctx;

    public SpacesService(Bus bus, Context ctx) {
        mBus=bus;
        this.ctx=ctx;
    }

    private final OkHttpClient client = new OkHttpClient();

    @Subscribe
    public void APIResponseSpaces(APIResponseSpacesEvent event) {
        Log.i(LOG_TAG, "APIResponseSpaces() SUBSCRIPTION APIResponseSpacesEvent");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject obj = null;
        try {
            obj = new JSONObject(event.getResponse());
            List<Space> spaces = Arrays.asList(gson.fromJson(obj.optString("spaces", "{}"), Space[].class));
            mBus.post(new SpacesLoadedEvent(spaces));

        } catch (Exception e) {
            e.printStackTrace();
            mBus.post(new APIErrorEvent(e.getMessage()));
        }
    }



    @Subscribe
    public void loadSpaces(LoadSpacesEvent event) throws Exception {
        Log.i(LOG_TAG, "loadSpaces() SUBSCRIPTION LoadSpacesEvent");

        mBus.post(new APIRequestSpacesEvent("spaces"));

    }
}
