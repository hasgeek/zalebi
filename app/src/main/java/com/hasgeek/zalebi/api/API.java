package com.hasgeek.zalebi.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hasgeek.zalebi.api.model.SyncQueueContact;
import com.hasgeek.zalebi.eventbus.event.api.APIErrorEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncAttendeesEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIRequestSyncContactsEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSingleSpaceEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSpacesEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSyncAttendeesEvent;
import com.hasgeek.zalebi.eventbus.event.api.APIResponseSyncContactsEvent;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;

/**
 * Created by karthik on 29-12-2014.
 */
public class API {

    private final static String LOG_TAG = "API";

    private final static String BASE_URL = "http://talkfunnel.com";

    private final static OkHttpClient client = new OkHttpClient();

    private final static String SPACES_ENDPOINT = BASE_URL + "/json";

    private final static String CONTACTEXCHANGESYNC_URL = "http://metarefresh.talkfunnel.com/2015/participant";

    private final static String ATTENDEESYNC_URL = "http://metarefresh.talkfunnel.com/2015/participants/json";

    private static Bus mBus;

    private static Context ctx;

    public API(Bus mBus, Context applicationContext) {
        this.mBus = mBus;
        this.ctx = applicationContext;
    }

    @Subscribe
    public void syncContacts(APIRequestSyncContactsEvent event) {
        Log.i(LOG_TAG, "syncContacts() SUBSCRIPTION APIRequestSyncContactsEvent");

        final String mURL = event.getSpaceId();

        for(final SyncQueueContact syncQueueContact: SyncQueueContact.listAll(SyncQueueContact.class)) {

            Request request = new Request.Builder()
                    .url(CONTACTEXCHANGESYNC_URL+"?puk="+syncQueueContact.getUserPuk()+"&key="+syncQueueContact.getUserKey())
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, final IOException e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            new APIErrorEvent(e.getMessage());
                        }
                    });
                }

                @Override
                public void onResponse(final Response response) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mBus.post(new APIResponseSyncContactsEvent(response.body().string(), syncQueueContact.getUserId()));
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

    @Subscribe
    public void syncAttendees(APIRequestSyncAttendeesEvent event) {
        Log.i(LOG_TAG, "syncAttendees() SUBSCRIPTION APIRequestSyncAttendeesEvent");

        Request request = new Request.Builder()
                .url(ATTENDEESYNC_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, final IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        new APIErrorEvent(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Response response) {
                try {
                    final String res = response.body().string();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mBus.post(new APIResponseSyncAttendeesEvent(res));
                            } catch (Exception e) {
                                e.printStackTrace();
                                mBus.post(new APIErrorEvent(e.getMessage()));
                            }
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                                mBus.post(new APIErrorEvent(e.getMessage()));
                        }
                    });
                }

            }
        });

    }


    @Subscribe
    public void getSpaces(APIRequestSpacesEvent event) {
        Log.i(LOG_TAG, "getSpaces() SUBSCRIPTION APIRequestSpacesEvent");
        Request request = new Request.Builder()
                .url(SPACES_ENDPOINT)
                .build();

        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(ctx.getMainLooper());

            @Override
            public void onFailure(Request request, final IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        new APIErrorEvent(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
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

    @Subscribe
    public void getSingleSpace(APIRequestSingleSpaceEvent event) {
        final String mURL = event.getSpace_id();
        Log.i(LOG_TAG, "getSingleSpace() SUBSCRIPTION APIRequestSingleSpaceEvent " + mURL);

        Request request = new Request.Builder()
                .url(mURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            Handler mainHandler = new Handler(ctx.getMainLooper());

            @Override
            public void onFailure(Request request, final IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        new APIErrorEvent(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mBus.post(new APIResponseSingleSpaceEvent(mURL, response.body().string()));
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
