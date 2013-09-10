package com.hasgeek.misc;

import java.io.Serializable;


public class EventSession implements Serializable {

    String id;
    String title;
    String speaker;
    String section;
    String level;
    String description;
    boolean bookmarked;


    public EventSession(String _id, String _title, String _speaker, String _section, String _level, String _desc, boolean _bookmarked) {
        setId(_id);
        setTitle(_title);
        setSpeaker(_speaker);
        setSection(_section);
        setLevel(_level);
        setDescription(_desc);
        setBookmarked(_bookmarked);
    }


    @Override
    public String toString() {
        return id + ": " + title + ": " + description;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getSpeaker() {
        return speaker;
    }


    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }


    public String getSection() {
        return section;
    }


    public void setSection(String section) {
        this.section = section;
    }


    public String getLevel() {
        return level;
    }


    public void setLevel(String level) {
        this.level = level;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isBookmarked() {
        return bookmarked;
    }


    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
