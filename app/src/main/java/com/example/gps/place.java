package com.example.gps;

public class place {
    String name;
    double latitude;
    double longitude;

    place(String name, double latitude, double longitude){
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
        if(other instanceof place){
            place that = (place) other;
            return this.name.equals(that.name) && this.latitude == that.latitude && this.longitude == that.longitude;
        }else {
            return false;
        }
    }
    @Override
    public int hashCode(){
        return toString().hashCode();
    }
    public static place parse(String s){
        String[] parts = s.split(":");
        return new place(parts[0],Double.parseDouble(parts[1]),Double.parseDouble(parts[2]));
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
