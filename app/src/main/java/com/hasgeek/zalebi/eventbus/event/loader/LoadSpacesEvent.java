package com.hasgeek.zalebi.eventbus.event.loader;

/**
 * Created by karthik on 24-12-2014.
 */
public class LoadSpacesEvent {
    private String message;

    public LoadSpacesEvent(String message) {
        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
