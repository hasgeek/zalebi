package com.hasgeek.zalebi.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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

            String access_token = uri.getQueryParameter("access_token"); // "str" is set
            String token_type = uri.getQueryParameter("token_type"); // "string" is set

            Log.d("LoginActivity", ""+access_token+" "+token_type);
        }
    }
}
