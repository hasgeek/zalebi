package com.hasgeek.zalebi.eventbus.event;

/**
 * Created by karthik on 29-12-2014.
 */
public class APIRequestSpacesEvent {
    String response;

    public APIRequestSpacesEvent(String response) {
        this.response = response;
    }

    public String getResponse() {

        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
