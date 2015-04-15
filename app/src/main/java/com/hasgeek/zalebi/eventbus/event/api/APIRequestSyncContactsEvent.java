package com.hasgeek.zalebi.eventbus.event.api;

/**
 * Created by karthikbalakrishnan on 01/04/15.
 */
public class APIRequestSyncContactsEvent {
    private String spaceId;
    private String spaceUrl;

    public APIRequestSyncContactsEvent(String spaceId, String spaceUrl) {
        this.spaceId = spaceId;
        this.spaceUrl = spaceUrl;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceUrl() {
        return spaceUrl;
    }

    public void setSpaceUrl(String spaceUrl) {
        this.spaceUrl = spaceUrl;
    }
}
