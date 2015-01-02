package com.hasgeek.zalebi.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.api.model.Proposal;
import com.hasgeek.zalebi.api.model.Section;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.event.api.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.loader.LoadSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.loader.LoadSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SingleSpaceLoadedEvent;
import com.hasgeek.zalebi.eventbus.event.loader.SpacesLoadedEvent;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by karthik on 24-12-2014.
 */
public class SpacesService {

    private String LOG_TAG = "SpacesService";
    private Bus mBus;
    Context ctx;
    SharedPreferences prefs;

    public SpacesService(Bus bus, Context ctx) {
        mBus=bus;
        this.ctx=ctx;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
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
            prefs.edit().putString("spaces_data", obj.toString()).commit();
            mBus.post(new LoadSpacesEvent("spaces"));

        } catch (Exception e) {
            e.printStackTrace();
            mBus.post(new APIErrorEvent(e.getMessage()));
        }
    }

    @Subscribe
    public void APIResponseSingleSpace(APIResponseSingleSpaceEvent event) {
        Log.i(LOG_TAG, "APIResponseSingleSpace() SUBSCRIPTION APIResponseSingleSpaceEvent");
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject obj = null;
        try {
            obj = new JSONObject(event.getResponse());
            prefs.edit().putString("space_data_" + event.getSpace_id(), obj.toString()).commit();
            mBus.post(new LoadSingleSpaceEvent(event.getSpace_id()));

        } catch (Exception e) {
            e.printStackTrace();
            mBus.post(new APIErrorEvent(e.getMessage()));
        }
    }

    @Subscribe
    public void loadSpaces(LoadSpacesEvent event) {
        Log.i(LOG_TAG, "loadSpaces() SUBSCRIPTION LoadSpacesEvent");

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject obj = null;
        try {
            obj = new JSONObject(prefs.getString("spaces_data", "{}"));
            List<Space> spaces = Arrays.asList(gson.fromJson(obj.optString("spaces", "{}"), Space[].class));
            mBus.post(new SpacesLoadedEvent(spaces));

        } catch (Exception e) {
            e.printStackTrace();
            mBus.post(new APIErrorEvent(e.getMessage()));
        }
    }

    @Subscribe
    public void loadSingleSpace(LoadSingleSpaceEvent event) {
        Log.i(LOG_TAG, "loadSingleSpace() SUBSCRIPTION LoadSingleSpaceEvent");

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject obj = null;
        try {
            obj = new JSONObject(prefs.getString("space_data_" + event.getSpace_id(), "{}"));
            List<Proposal> proposals = Arrays.asList(gson.fromJson(obj.optString("proposals", "{}"), Proposal[].class));
            List<Section> sections = Arrays.asList(gson.fromJson(obj.optString("sections", "{}"), Section[].class));
            Space space = gson.fromJson(obj.optString("space", "{}"), Space.class);
            mBus.post(new SingleSpaceLoadedEvent(proposals, sections, space));

        } catch (Exception e) {
            e.printStackTrace();
            mBus.post(new APIErrorEvent(e.getMessage()));
        }
    }
}
