package com.hasgeek.zalebi.eventbus.event.api;

/**
 * Created by karthikbalakrishnan on 01/04/15.
 */
public class APIResponseSyncContactsEvent {
    private String response;
    private String userId;

    public APIResponseSyncContactsEvent(String response, String userId) {
        this.response = response;
        this.userId = userId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
