package com.hasgeek.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.hasgeek.R;
import com.hasgeek.bus.BusProvider;
import com.hasgeek.bus.JSFooAPICalledEvent;
import com.hasgeek.bus.SessionFeedbackAlreadySubmittedEvent;
import com.hasgeek.bus.SessionFeedbackSubmittedEvent;
import com.hasgeek.misc.DataProvider;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;


public class APIService extends IntentService {

    public static final String MODE = "APIService.MODE";
    public static final String RESPONSE_CODE = "APIService.RESPONSE_CODE";
    public static final String RESPONSE_DATA = "APIService.RESPONSE_DATA";
    public static final String SYNC_JSFOO = "APIService.SYNC_JSFOO";
    public static final String POST_FEEDBACK = "APIService.POST_FEEDBACK";

    private static final String API_BASE = "https://funnel.hasgeek.com";
    private static final String TAG = "HasGeek";


    public APIService() {
        super("APIService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String mode = intent.getStringExtra(MODE);
        ContentResolver cr = getContentResolver();

        if (mode.equals(SYNC_JSFOO)) {
            JSFooAPICalledEvent event = new JSFooAPICalledEvent();
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
                                DataProvider.PROPOSAL_URI,
                                new String[] { "id" },
                                "id is ?",
                                new String[] { String.valueOf(pro.getInt("id")) },
                                null
                        );
                        if (idCheck.moveToFirst() && (idCheck.getCount() == 1)) {
                            cr.update(DataProvider.PROPOSAL_URI, cv, "id is ?", new String[] { String.valueOf(pro.getInt("id")) });
                        } else {
                            Uri u = cr.insert(DataProvider.PROPOSAL_URI, cv);
                            cr.notifyChange(u, null);
                        }
                        idCheck.close();
                    }
                } else {
                    event.setMessage(getString(R.string.message_apifailed));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to parse JSFoo JSON");

            } finally {
                BusProvider.getInstance().post(event);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().putBoolean("first_run", false).commit();
            }

        } else if (mode.equals(APIService.POST_FEEDBACK)) {
            BasicNameValuePair[] pairs = {
                    new BasicNameValuePair("id_type", "email"), //todo hard-coded for now, change later?
                    new BasicNameValuePair("userid", intent.getStringExtra("userid")),
                    new BasicNameValuePair("content", intent.getStringExtra("content")),
                    new BasicNameValuePair("presentation", intent.getStringExtra("presentation")),
            };

            HashMap<String, String> response = runHTTPPostRequest(intent.getStringExtra("url"), pairs);
            if (response.get(RESPONSE_CODE).equals("201")) {
                BusProvider.getInstance().post(new SessionFeedbackSubmittedEvent());
            } else if (response.get(RESPONSE_CODE).equals("403")) {
                BusProvider.getInstance().post(new SessionFeedbackAlreadySubmittedEvent());
            }

        } else {
            throw new RuntimeException("The service isn't sure what to do with this");
        }
    }


    private HttpCodeAndResponse runOkHttpGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        HttpCodeAndResponse codeAndResponse = new HttpCodeAndResponse();

        try {
            HttpURLConnection connection = client.open(new URL(url));
            // Read the response.
            InputStream in = connection.getInputStream();
            codeAndResponse.setCode(String.valueOf(connection.getResponseCode()));
            byte[] response = readFully(in);
            codeAndResponse.setResponse(new String(response));
            if (in != null) in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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


    /**
     * Runs an HTTP POST request, with a set of key-value pairs as POST data.
     */
    public static HashMap<String, String> runHTTPPostRequest(String URL, BasicNameValuePair[] params) {
        HashMap<String, String> returnValue = new HashMap<String, String>();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;
        HttpPost request = new HttpPost(URL);

        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(Arrays.asList(params), HTTP.UTF_8);
            urlEncodedFormEntity.setContentEncoding(HTTP.UTF_8);
            request.setEntity(urlEncodedFormEntity);
            request.addHeader("Accept-Encoding", "gzip");

            // Making connection now...
            httpResponse = httpClient.execute(request);
            returnValue.put(RESPONSE_CODE, String.valueOf(httpResponse.getStatusLine().getStatusCode()));

            switch (httpResponse.getStatusLine().getStatusCode()) {
                case 200:
                case 201:
                    if (httpResponse.getEntity() != null) {
                        InputStream in = httpResponse.getEntity().getContent();
                        Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
                        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                            in = new GZIPInputStream(in);
                        }
                        returnValue.put(RESPONSE_DATA, convertStreamToString(in));
                        in.close();
                    }
                    break;

                case 404:
                    // not found
                    Log.w(TAG, "HTTP 404 not found");
                    break;

                case 403:
                    // access denied
                    Log.w(TAG, "HTTP 403 access denied");
                    break;

                default:
                    // lol
            }

        } catch (ClientProtocolException e) {
            // Error with the HTTP protocol (?!)
            e.printStackTrace();
            returnValue.put(RESPONSE_CODE, "000");

        } catch (IOException e) {
            // Connection time-out or server not found
            e.printStackTrace();
            returnValue.put(RESPONSE_CODE, "000");

        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return returnValue;
    }


    /**
     * Converts an InputStream to a String.
     */
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            // foofy
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // foofy
            }
        }
        return sb.toString();
    }

}
