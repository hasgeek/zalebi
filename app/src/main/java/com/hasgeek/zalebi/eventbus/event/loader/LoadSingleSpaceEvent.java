package com.hasgeek.zalebi.eventbus.event.loader;

/**
 * Created by karthik on 30-12-2014.
 */
public class LoadSingleSpaceEvent {
    String space_id;

    public String getSpace_id() {
        return space_id;
    }

    public void setSpace_id(String space_id) {
        this.space_id = space_id;
    }

    public LoadSingleSpaceEvent(String message) {

        this.space_id = message;
    }
}
