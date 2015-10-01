package com.hasgeek.zalebi.eventbus.event.contactexchange;

/**
 * Created by karthikbalakrishnan on 30/03/15.
 */
public class ContactScannedEvent {
    String data;

    public ContactScannedEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
