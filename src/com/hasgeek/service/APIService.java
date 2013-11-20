package com.hasgeek.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.hasgeek.R;
import com.hasgeek.bus.BusProvider;
import com.hasgeek.bus.DroidconAPICalledEvent;
import com.hasgeek.bus.SessionFeedbackAlreadySubmittedEvent;
import com.hasgeek.bus.SessionFeedbackSubmittedEvent;
import com.hasgeek.misc.DataProvider;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class APIService extends IntentService {

    public static final String MODE = "APIService.MODE";
    public static final String SYNC_DROIDCON2013 = "APIService.SYNC_DROIDCON2013";
    public static final String POST_FEEDBACK = "APIService.POST_FEEDBACK";

    private static final String API_BASE = "https://funnel.hasgeek.com";


    public APIService() {
        super("APIService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String mode = intent.getStringExtra(MODE);
        ContentResolver cr = getContentResolver();

        if (mode.equals(SYNC_DROIDCON2013)) {
            DroidconAPICalledEvent event = new DroidconAPICalledEvent();
            try {
                HttpCodeAndResponse reply = runOkHttpGetRequest(API_BASE + "/jsfoo2013/json");
                if (reply.getCode().equals("200")) {
                    JSONObject j = new JSONObject(reply.getResponse());
                    JSONArray proposals = j.getJSONArray("proposals");

                    for (int i = 0; i < proposals.length(); i++) {
                        JSONObject pro = proposals.getJSONObject(i);

                        ContentValues cv = new ContentValues();
                        cv.put("id", pro.getInt("id"));
                        cv.put("title", pro.getString("title"));
                        cv.put("speaker", pro.getString("speaker"));
                        cv.put("section", pro.getString("section"));
                        cv.put("level", pro.getString("level"));
                        cv.put("description", pro.getString("description"));
                        cv.put("url", pro.getString("url"));

                        // Check if proposal with this id already exists or not
                        Cursor idCheck = cr.query(
                                DataProvider.SESSION_URI,
                                new String[] { "id" },
                                "id is ?",
                                new String[] { String.valueOf(pro.getInt("id")) },
                                null
                        );
                        if (idCheck.moveToFirst() && (idCheck.getCount() == 1)) {
                            cr.update(DataProvider.SESSION_URI, cv, "id is ?", new String[] { String.valueOf(pro.getInt("id")) });
                        } else {
                            Uri u = cr.insert(DataProvider.SESSION_URI, cv);
                            cr.notifyChange(u, null);
                        }
                        idCheck.close();
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                        sp.edit().putBoolean("first_run", false).commit();
                    }
                } else {
                    event.setMessage(getString(R.string.message_apifailed));
                }
                // Done with everything, post to bus
                BusProvider.getInstance().post(event);

            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to parse JSFoo JSON");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (mode.equals(APIService.POST_FEEDBACK)) {
            String charset = "UTF-8";
            String params = null;
            try {
                params = String.format("id_type=%s&userid=%s&content=%s&presentation=%s",
                        URLEncoder.encode("email", charset),
                        URLEncoder.encode(intent.getStringExtra("userid"), charset),
                        URLEncoder.encode(intent.getStringExtra("content"), charset),
                        URLEncoder.encode(intent.getStringExtra("presentation"), charset));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                HttpCodeAndResponse response = runOkHttpPostRequest(intent.getStringExtra("url"), params);
                if (response.getCode().equals("201")) {
                    BusProvider.getInstance().post(new SessionFeedbackSubmittedEvent());
                } else if (response.getCode().equals("403")) {
                    BusProvider.getInstance().post(new SessionFeedbackAlreadySubmittedEvent());
                } else {
                    BusProvider.getInstance().post(new SessionFeedbackSubmittedEvent(getString(R.string.message_apifailed)));
                }
            } catch (IOException e) {
                BusProvider.getInstance().post(new SessionFeedbackSubmittedEvent(getString(R.string.message_apifailed)));
                e.printStackTrace();
            }

        } else {
            throw new RuntimeException("The service isn't sure what to do with this");
        }
    }


    private HttpCodeAndResponse runOkHttpGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpCodeAndResponse codeAndResponse = new HttpCodeAndResponse();
        InputStream in = null;
        try {
            HttpURLConnection connection = client.open(new URL(url));
            codeAndResponse.setCode(String.valueOf(connection.getResponseCode()));
            in = connection.getInputStream();
            byte[] response = readFully(in);
            codeAndResponse.setResponse(new String(response));

        } finally {
            if (in != null) in.close();
        }

        return codeAndResponse;
    }


    private byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }


    private HttpCodeAndResponse runOkHttpPostRequest(String url, String body) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpCodeAndResponse codeAndResponse = new HttpCodeAndResponse();
        OutputStreamWriter out = null;
        InputStream in = null;

        try {
            HttpURLConnection connection = client.open(new URL(url));
            connection.setRequestMethod("POST");
            out = new OutputStreamWriter(connection.getOutputStream());
            out.write(body);
            out.close();

            codeAndResponse.setCode(String.valueOf(connection.getResponseCode()));
            if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                // There's no need to process inputstream if server doesn't return 201
                in = connection.getInputStream();
                codeAndResponse.setResponse(readFirstLine(in));
            }

        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }

        return codeAndResponse;
    }


    String readFirstLine(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        return reader.readLine();
    }
}
