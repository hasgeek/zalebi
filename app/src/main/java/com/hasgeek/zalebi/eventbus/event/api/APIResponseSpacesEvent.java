package com.hasgeek.zalebi.eventbus.event.api;

/**
 * Created by karthik on 29-12-2014.
 */
public class APIResponseSpacesEvent {
    String response;

    public APIResponseSpacesEvent(String response) {
        this.response = response;
    }

    public String getResponse() {

        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
