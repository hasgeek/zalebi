package com.hasgeek.zalebi.eventbus.event.api;

/**
 * Created by karthikbalakrishnan on 01/04/15.
 */
public class APIResponseSyncAttendeesEvent {
    private String response;

    public APIResponseSyncAttendeesEvent(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
