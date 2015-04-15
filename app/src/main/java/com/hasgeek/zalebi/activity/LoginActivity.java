package com.hasgeek.zalebi.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.hasgeek.zalebi.api.AuthService;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by karthikbalakrishnan on 09/04/15.
 */
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        // check if this intent is started via custom scheme link
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            Uri uri = Uri.parse("talkfunnel://login?"+intent.getData().getFragment());
            // may be some test here with your custom uri

            final String access_token = uri.getQueryParameter("access_token");
            String token_type = uri.getQueryParameter("token_type");

            Log.d("LoginActivity", ""+access_token+" "+token_type);

            final Request request = new Request.Builder()
                    .url("https://talkfunnel.com/api/whoami")
                    .addHeader("Authorization", "Bearer "+access_token)
                    .build();
            OkHttpClient client = new OkHttpClient();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, final IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onResponse(final Response response) {
                    try {
                        String res =response.body().string();
                        Log.d("Response", res);
                        JSONObject json = new JSONObject(res);
                        if(json.optInt("code", 0)==200) {
                            AuthService.saveUserToken(access_token);
                            Intent i = new Intent(LoginActivity.this, SpacesActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                            startActivity(i);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            });
        }
    }
}
