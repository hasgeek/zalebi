package com.hasgeek.zalebi.eventbus.event;

import android.util.Log;

import com.hasgeek.zalebi.api.model.Space;

import java.util.List;

/**
 * Created by karthik on 24-12-2014.
 */
public class SpacesLoadedEvent {
    String LOG_TAG = "SpacesLoadedEvent";

    private List<Space> spaces;

    public SpacesLoadedEvent(List<Space> spaces) {
        this.spaces = spaces;
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Space> spaces) {
        this.spaces=spaces;
    }
}
