package com.example.gps;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void pathToStringWorking() {

        assertEquals("bob:1.0:2.0;tim:1.0:2.0;255", new Path(new Place("bob",1,2),new Place("tim",1,2),255).toString());
    }
    @Test
    public void something(){
        Place place = new Place("House",1,2);
        final DataManager manager = new DataManager(getFilesDir());
        ArrayList<Place> placeList= new ArrayList<>();
        try {
            manager.savePlace(place);
        }catch(Exception e){
            ModalDialogs.notifyException(ExampleUnitTest.this,e);
        }
        try{
            manager.loadAllPlaces();
        }catch(Exception e){
            ModalDialogs.notifyException(ExampleUnitTest.this,e);
        }


        assertEquals(place, );
    }
}