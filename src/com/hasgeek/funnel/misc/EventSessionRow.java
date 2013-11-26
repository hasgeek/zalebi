package com.hasgeek.funnel.misc;

import java.util.List;


public class EventSessionRow {

    String dateInIst;
    String timeslotInIst24Hrs;
    List<EventSession> sessions;


    public EventSessionRow(String _date, String _time, List<EventSession> _sessions) {
        setDateInIst(_date);
        setTimeslotInIst24Hrs(_time);
        setSessions(_sessions);
    }


    public String getDateInIst() {
        return dateInIst;
    }


    public void setDateInIst(String dateInIst) {
        this.dateInIst = dateInIst;
    }


    public String getTimeslotInIst24Hrs() {
        return timeslotInIst24Hrs;
    }


    public void setTimeslotInIst24Hrs(String timeslotInIst24Hrs) {
        this.timeslotInIst24Hrs = timeslotInIst24Hrs;
    }


    public List<EventSession> getSessions() {
        return sessions;
    }


    public void setSessions(List<EventSession> sessions) {
        this.sessions = sessions;
    }
}
