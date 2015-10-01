package com.hasgeek.zalebi.eventbus.event.api;

/**
 * Created by karthik on 24-12-2014.
 */
public class APIErrorEvent {
    private String message;

    public APIErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
