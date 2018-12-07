package com.dimovski.sportko.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import com.dimovski.sportko.db.converter.Converters;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "events")
@TypeConverters(Converters.class)
public class Event {


    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String description;
    private Date created;
    private Date scheduled;
    private double lat;
    private double lon;
    private String locationName;
    private String imgSrc;
    private int maxAttendees;
    private String typeOfEvent;
    private String createdBy;
    private ArrayList<String> attendees;
    private String city;

    public Event() {}

    @Ignore
    public Event(@NonNull  String id) {
        this.id = id;
    }

    @Ignore
    public Event(String title, String description, Date created, Date scheduled, double lat, double lon, String locationName, String imgSrc, int maxAttendees, String typeOfEvent
    ,String createdBy, String city) {
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
        this.city = city;
        attendees.add(createdBy);
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
