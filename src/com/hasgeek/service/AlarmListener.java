package com.hasgeek.service;

import android.content.Intent;
import android.util.Log;

import com.commonsware.cwac.wakeful.WakefulIntentService;


public class AlarmListener extends WakefulIntentService {


    public AlarmListener() {
        super("AlarmListener");
    }


    @Override
    protected void doWakefulWork(Intent intent) {
        Log.e("ASDASDASD", "doWakefulWork was called");
    }
}
