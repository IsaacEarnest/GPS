package com.example.gps;

public class Path {
    Place start;
    Place destination;
    int time;
    Path(Place start, Place destination, int time){
        this.start = start;
        this.destination = destination;
        this.time = time;
    }
    @Override
    public String toString(){ return (this.start.toString()+";"+this.destination.toString()+";"+time); }
    @Override
    public boolean equals(Object other){
        if(other instanceof Path){
            Path that = (Path) other;
            return this.start.equals(that.start) && this.destination == that.destination && this.time == that.time;
        }else {
            return false;
        }
    }
    @Override
    public int hashCode(){ return toString().hashCode(); }

    public static Path parse(String s){
        String[] parts = s.split(";");
        return new Path(Place.parse(parts[0]), Place.parse(parts[1]),Integer.parseInt(parts[2]));
    }
    public int getTime(){
        return time;
    }
    public Place getDestination(){
        return destination;
    }
    public Place getStart(){
        return start;
    }
}
