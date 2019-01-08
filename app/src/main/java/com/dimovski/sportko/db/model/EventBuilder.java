package com.dimovski.sportko.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**Builder pattern for creating Event objects*/
public class EventBuilder {

    private String id;
    private String title;
    private String description;
    private Date created;
    private Date scheduled;
    private double lat;
    private double lon;
    private String locationName;
    private String city;
    private String imgSrc;
    private int maxAttendees;
    private String typeOfEvent;
    private String createdBy;
    private ArrayList<String> attendees;

    public EventBuilder() {}

    public EventBuilder id(String id) {
        this.id = id;
        return this;
    }

    public EventBuilder title(String title) {
        this.title = title;
        return this;
    }

    public EventBuilder description (String description) {
        this.description =description;
        return this;
    }

    public EventBuilder created(Date created) {
        this.created = created;
        return this;
    }

    public EventBuilder scheduled (Date scheduled) {
        this.scheduled = scheduled;
        return this;
    }

    public EventBuilder location (LocationDetails details) {
        this.lat = details.getLat();
        this.lon = details.getLon();
        this.locationName = details.getLocationName();
        this.city = details.getCity();
        return this;
    }

    public EventBuilder imgSrc (String imgSrc){
        this.imgSrc = imgSrc;
        return this;
    }

    public EventBuilder maxAttendees (int maxAttendees){
        this.maxAttendees = maxAttendees;
        return this;
    }

    public EventBuilder typeOfEvent (String typeOfEvent){
        this.typeOfEvent = typeOfEvent;
        return this;
    }

    public EventBuilder createdBy (String createdBy){
        this.createdBy =createdBy;
        return this;
    }

    public EventBuilder attendees (ArrayList<String> attendees) {
        this.attendees = attendees;
        return this;
    }

    public Event createEvent() {
        Event e = new Event(title,description,created,scheduled,lat,lon,locationName,imgSrc,maxAttendees,typeOfEvent,createdBy,city);
        if (attendees != null) e.setAttendees(attendees);
        if (id !=null) e.setId(id);
        return e;
    }



}
