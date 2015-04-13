package com.hasgeek.zalebi.eventbus.event.api;

/**
 * Created by karthikbalakrishnan on 01/04/15.
 */
public class APIResponseSyncContactsEvent {
    private String response;

    public APIResponseSyncContactsEvent(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
