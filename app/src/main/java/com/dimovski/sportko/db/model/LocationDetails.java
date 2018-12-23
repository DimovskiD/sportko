package com.dimovski.sportko.db.model;

public class LocationDetails {

    private double lat;
    private double lon;
    private String locationName;
    private String city;

    public LocationDetails(double lat, double lon, String locationName, String city) {
        this.lat = lat;
        this.lon = lon;
        this.locationName = locationName;
        this.city = city;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
