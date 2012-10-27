package com.hasgeek.activity;

import android.app.Activity;
import android.os.Bundle;
import com.hasgeek.R;

public class EventDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetail);

        setTitle(getIntent().getStringExtra("name"));
    }

}
