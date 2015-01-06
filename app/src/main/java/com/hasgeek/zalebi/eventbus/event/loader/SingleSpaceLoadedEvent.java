package com.hasgeek.zalebi.eventbus.event.loader;

import com.hasgeek.zalebi.api.model.Proposal;
import com.hasgeek.zalebi.api.model.Room;
import com.hasgeek.zalebi.api.model.Section;
import com.hasgeek.zalebi.api.model.Space;
import com.hasgeek.zalebi.api.model.Venue;

import java.util.List;

/**
 * Created by karthik on 30-12-2014.
 */
public class SingleSpaceLoadedEvent {

    private List<Proposal> proposals;
    private List<Section> sections;
    private List<Room> rooms;
    private List<Venue> venues;
    private Space space;

    public SingleSpaceLoadedEvent(List<Proposal> proposals, List<Section> sections, List<Room> rooms, List<Venue> venues, Space space) {
        this.proposals = proposals;
        this.sections = sections;
        this.rooms = rooms;
        this.venues = venues;
        this.space = space;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public List<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }


}
