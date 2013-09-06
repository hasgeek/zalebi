package com.hasgeek.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import com.hasgeek.misc.DBManager;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

public class APIService extends IntentService {

    private static final String API_BASE = "http://101.62.125.61:6400/api";
    private static final String VERSION = "/v1";
    private static final String API = API_BASE + VERSION;

    private static final String EVENTS = API + "/events";
    private static final String TAG = "HasGeek";

    public static final String MODE = "APIService.MODE";
    public static final String SYNC_EVERYTHING = "APIService.SYNC_EVERYTHING";
    public static final String SYNC_EVERYTHING_DONE = "APIService.SYNC_EVERYTHING_DONE";
    public static final String RESPONSE_CODE = "APIService.RESPONSE_CODE";
    public static final String RESPONSE_DATA = "APIService.RESPONSE_DATA";


    public APIService() {
        super("APIService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String mode = intent.getStringExtra(MODE);

        if (mode.equals(SYNC_EVERYTHING)) {
            DBManager dbm = new DBManager(this);
            SQLiteDatabase db = dbm.getWritableDatabase();

            HashMap<String, String> response;

            try {
                db.beginTransaction();
                // Pull in events...
                response = runHTTPGetRequest(EVENTS);

                if (response.get(RESPONSE_CODE).equals("200")) {

                    JSONObject j = new JSONObject(response.get(RESPONSE_DATA));
                    JSONArray events = j.getJSONArray("events");

                    for (int i = 0; i < events.length(); i++) {
                        JSONObject ev = events.getJSONObject(i);
                        JSONObject geo = ev.getJSONObject("geo_location");

                        ContentValues cv = new ContentValues();
                        cv.put("hasgeekId", ev.getInt("id"));
                        cv.put("name", ev.getString("name"));
                        cv.put("location", ev.getString("location"));
                        cv.put("rootUrl", ev.getString("root_url"));
                        cv.put("lat", Float.parseFloat(geo.getString("lat")));
                        cv.put("long", Float.parseFloat(geo.getString("long")));
                        cv.put("startDatetime",  ev.getString("start_datetime"));
                        cv.put("endDatetime",  ev.getString("end_datetime"));

                        db.insertOrThrow(DBManager.PROPOSALS_TABLE, null, cv);
                    }
                }

                // Now let's pull in sessions


                // All done
                db.setTransactionSuccessful();

            } catch (JSONException e) {
                e.printStackTrace();

            } finally {
                db.endTransaction();
                db.close();

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().putLong("last_sync_time", System.currentTimeMillis()).commit();

                sendBroadcast(new Intent(SYNC_EVERYTHING_DONE));
                Log.w(TAG, "sent bcast");
            }
        }

    }


    /**
     * Runs an HTTP GET request.
     * Always adds the X-AUTH-TOKEN as an HTTP header.
     */
    public static HashMap<String, String> runHTTPGetRequest(String URL) {
        HashMap<String, String> returnValue = new HashMap<String, String>();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse;

        try {
            HttpGet request = new HttpGet();
            request.setURI(new URI(URL));

            request.addHeader("Accept-Encoding", "gzip");

            // Connection established over here
            httpResponse = httpClient.execute(request);
            returnValue.put(RESPONSE_CODE, String.valueOf(httpResponse.getStatusLine().getStatusCode()));

            switch (httpResponse.getStatusLine().getStatusCode()) {
                case 200:
                    // life is good
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

        } catch (IOException e) {
            // Server failed to respond with a valid HTTP response, or server not found
            e.printStackTrace();
            returnValue.put(RESPONSE_CODE, "000");

        } catch (URISyntaxException e) {
            // Error in creating the URI
            e.printStackTrace();
            returnValue.put(RESPONSE_CODE, "000");

        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return returnValue;
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
