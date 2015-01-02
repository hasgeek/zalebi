package com.hasgeek.zalebi.eventbus.event.loader;

import com.hasgeek.zalebi.api.model.Proposal;
import com.hasgeek.zalebi.api.model.Section;
import com.hasgeek.zalebi.api.model.Space;

import java.util.List;

/**
 * Created by karthik on 30-12-2014.
 */
public class SingleSpaceLoadedEvent {

    private List<Proposal> proposals;
    private List<Section> sections;
    private Space space;

    public SingleSpaceLoadedEvent(List<Proposal> proposals, List<Section> sections, Space space) {

        this.proposals = proposals;
        this.sections = sections;
        this.space = space;
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
