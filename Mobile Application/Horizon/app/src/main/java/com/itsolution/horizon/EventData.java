package com.itsolution.horizon;

class EventData {
    // Other fields and getters are the same as before
    private GeometryData[] geometries;

    public EventData(String eventId, String eventTitle, String eventDescription, String eventLink,
                     GeometryData[] geometries) {
        // Initialize fields
        this.geometries = geometries;
    }

    public GeometryData[] getGeometries() {
        return geometries;
    }
}

class GeometryData {
    private String date;
    private String type;
    private double[] coordinates;

    public GeometryData(String date, String type, double[] coordinates) {
        this.date = date;
        this.type = type;
        this.coordinates = coordinates;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }
}