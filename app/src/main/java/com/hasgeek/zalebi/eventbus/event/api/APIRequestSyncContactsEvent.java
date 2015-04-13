package com.hasgeek.zalebi.eventbus.event.api;

/**
 * Created by karthikbalakrishnan on 01/04/15.
 */
public class APIRequestSyncContactsEvent {
    private String spaceId;

    public APIRequestSyncContactsEvent(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }
}
