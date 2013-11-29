package com.hasgeek.funnel.bus;

import android.text.TextUtils;


public class SessionFeedbackSubmittedEvent {

    String message;

    public SessionFeedbackSubmittedEvent() {

    }

    public SessionFeedbackSubmittedEvent(String msg) {
        setMessage(msg);
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public boolean hasMessage() {
        return !TextUtils.isEmpty(message);
    }
}
