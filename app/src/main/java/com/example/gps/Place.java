package com.example.gps;

public class Place {
    String name;
    double latitude;
    double longitude;

    Place(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Override
    public String toString(){
        return (this.name+":"+this.latitude+":"+this.longitude);
    }
    @Override
    public boolean equals(Object other){
        if(other instanceof Place){
            Place that = (Place) other;
            return this.name.equals(that.name) && this.latitude == that.latitude && this.longitude == that.longitude;
        }else {
            return false;
        }
    }
    @Override
    public int hashCode(){
        return toString().hashCode();
    }
    public static Place parse(String s){
        String[] parts = s.split(":");
        return new Place(parts[0],Double.parseDouble(parts[1]),Double.parseDouble(parts[2]));
    }

    public String getName(){
        return this.name;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public double getLongitude(){
        return this.longitude;
    }

}
