package com.dimovski.sportko.db.model;

import java.util.Date;

public class Event {

    private int id;
    private String title;
    private String description;
    private Date created;
    private Date scheduled;
    private long lat;
    private long lon;
    private String locationName;

    public Event() {}

    public Event(int id) {
        this.id = id;
    }

    public Event(int id, String title, String description, Date created, Date scheduled, long lat, long lon, String locationName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created = created;
        this.scheduled = scheduled;
        this.lat = lat;
        this.lon = lon;
        this.locationName = locationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
