package com.hasgeek.bus;

import android.text.TextUtils;


public class JSFooAPICalledEvent {
    private String message;


    public void setMessage(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public boolean hasMessage() {
        return !TextUtils.isEmpty(message);
    }
}
