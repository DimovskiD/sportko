package com.dimovski.sportko.db.model;

import java.util.ArrayList;
import java.util.Date;

public class Event {

    private String id;
    private String title;
    private String description;
    private Date created;
    private Date scheduled;
    private long lat;
    private long lon;
    private String locationName;
    private String imgSrc;
    private int maxAttendees;
    private String typeOfEvent;
    private String createdBy;
    private ArrayList<String> attendees;


    public Event() {}

    public Event(String id) {
        this.id = id;
    }

    public Event(String title, String description, Date created, Date scheduled, long lat, long lon, String locationName, String imgSrc, int maxAttendees, String typeOfEvent
    ,String createdBy) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.scheduled = scheduled;
        this.lat = lat;
        this.lon = lon;
        this.locationName = locationName;
        this.imgSrc = imgSrc;
        this.maxAttendees = maxAttendees;
        this.typeOfEvent = typeOfEvent;
        this.createdBy = createdBy;
        this.attendees = new ArrayList<>(maxAttendees);
        attendees.add(createdBy);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getScheduled() {
        return scheduled;
    }

    public void setScheduled(Date scheduled) {
        this.scheduled = scheduled;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }

    public void setMaxAttendees(int maxAttendees) {
        this.maxAttendees = maxAttendees;
    }
    public String getTypeOfEvent() {
        return typeOfEvent;
    }

    public void setTypeOfEvent(String typeOfEvent) {
        this.typeOfEvent = typeOfEvent;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public void addAttendee(String attendee){
        attendees.add(attendee);
    }

    public void removeAtendee(String attendee) {
        attendees.remove(attendee);
    }
}
