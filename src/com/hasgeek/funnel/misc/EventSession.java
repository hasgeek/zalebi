package com.hasgeek.funnel.misc;

import java.io.Serializable;


public class EventSession implements Serializable {

    String id;
    String title;
    String speaker;
    String section;
    String level;
    String description;
    String url;
    String dateInIst;
    String slotInIst24Hrs;
    String roomTitle;
    String roomColor;
    boolean bookmarked;


    public EventSession(String _id, String _title, String _speaker,
                        String _section, String _level, String _desc,
                        String _url, String _date_ist, String _slot_ist,
                        String _room_title, String _room_color,
                        boolean _bookmarked) {
        setId(_id);
        setTitle(_title);
        setSpeaker(_speaker);
        setSection(_section);
        setLevel(_level);
        setDescription(_desc);
        setUrl(_url);
        setDateInIst(_date_ist);
        setSlotInIst24Hrs(_slot_ist);
        setBookmarked(_bookmarked);
        setRoomTitle(_room_title);
        setRoomColor(_room_color);
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


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getDateInIst() {
        return dateInIst;
    }


    public void setDateInIst(String dateInIst) {
        this.dateInIst = dateInIst;
    }


    public String getSlotInIst24Hrs() {
        return slotInIst24Hrs;
    }


    public void setSlotInIst24Hrs(String slotInIst24Hrs) {
        this.slotInIst24Hrs = slotInIst24Hrs;
    }


    public String getRoomTitle() {
        return roomTitle;
    }


    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }


    public String getRoomColor() {
        return roomColor;
    }


    public void setRoomColor(String roomColor) {
        this.roomColor = roomColor;
    }
}
