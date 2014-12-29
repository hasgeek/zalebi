package com.hasgeek.zalebi.api;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.eventbus.event.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.APIRequestSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.APIResponseSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.SpacesLoadedEvent;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karthik on 29-12-2014.
 */
public class API {

    private final static String LOG_TAG="API";

    private final static String BASE_URL="http://talkfunnel.com";

    private final static OkHttpClient client = new OkHttpClient();

    private final static String SPACES_ENDPOINT=BASE_URL+"/json";

    private static Bus mBus;

    private static Context ctx;

    public void init(Bus mBus, Context applicationContext) {
        this.mBus=mBus;
        this.ctx=applicationContext;
    }

    @Subscribe
    public static void getSpaces(APIRequestSpacesEvent event)
    {
        Log.i(LOG_TAG, "getSpaces() SUBSCRIPTION APIRequestSpacesEvent");
        Request request = new Request.Builder()
                .url(SPACES_ENDPOINT)
                .build();

        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(ctx.getMainLooper());
            @Override
            public void onFailure(Request request, final IOException e) {
                e.printStackTrace();
                new APIErrorEvent(e.getMessage());
            }

            @Override
            public void onResponse(final Response response) {
                new Handler().post(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            mBus.post(new APIResponseSpacesEvent(response.body().string()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            mBus.post(new APIErrorEvent(e.getMessage()));
                        }
                    }
                });

            }
        });
    }


}
