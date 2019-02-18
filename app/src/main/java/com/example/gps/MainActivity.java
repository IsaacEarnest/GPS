package com.example.gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText newLocationName;
    private Spinner fromHere;
    private Spinner toHere;
    private int timeToDestination;
    private Chronometer chronometer;
    private boolean isTimerRunning;
    private long pauseOffset;
    private List<String> list;
    private double longitude;
    private double latitude;
    private Place destination;
    private Place start;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton savePlaces = findViewById(R.id.savePlaces);
        final DataManager manager = new DataManager(getFilesDir());
        textView = findViewById(R.id.textView);
        Button resetButton = findViewById(R.id.resetButton);
        ImageButton settings = findViewById(R.id.settings);
        chronometer = findViewById(R.id.chronometer);
        Button addLocation = findViewById(R.id.addLocation);
        newLocationName = findViewById(R.id.newLocationName);
        fromHere = findViewById(R.id.fromHere);
        toHere = findViewById(R.id.toHere);
        Button startJourney = findViewById(R.id.startJourney);
        list = new ArrayList<>();
        isTimerRunning = false;
        list.add("Select a location");
        latitude = -1;
        longitude = -1;
        timeToDestination = -1;

        try {
            ArrayList<Place> places = manager.loadAllPlaces();
           for(Place p: places){
               list.add(p.getName());
           }
        }catch(Exception e){
            ModalDialogs.notifyException(MainActivity.this,e);
        }
        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resetTimer();
                displayResults();
            }
        });
        savePlaces.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
        startJourney.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startTimer();
                setJourney(manager);
                displayCoords();

            }
        });

        //initializing spinners
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromHere.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toHere.setAdapter(dataAdapter2);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace(manager);
            }
        });
        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openSettings();
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (isTimerRunning) checkLocation(manager);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager.requestLocationUpdates("gps", 500, 0, locationListener);

    }
    private void displayCoords(){
        textView.setText("Latitude = "+latitude+", Longitude = "+longitude+" Goal "+destination.getLatitude()+", "+destination.getLongitude());
    }
    private void addPlace(DataManager manager) {
        String value = newLocationName.getText().toString().trim();
        if (!value.equals("")) {
            list.add(value);
           try {
                manager.savePlace(new Place(value,latitude,longitude));
            }catch(Exception e){
                ModalDialogs.notifyException(MainActivity.this,e);
            }
            resetAddLocation();
            String addLocationSuccess = " has been Added!";
            Toast.makeText(getApplicationContext(),""+value+addLocationSuccess,Toast.LENGTH_SHORT).show();
        }else{
            String addLocationNoName = "Please type a name for new location.";
            Toast.makeText(getApplicationContext(),addLocationNoName,Toast.LENGTH_SHORT).show();
        }
    }
    private void resetAddLocation(){
        newLocationName.onEditorAction(EditorInfo.IME_ACTION_DONE);
        newLocationName.setText("");
    }
    private void checkLocation(DataManager manager){
        Log.i("MainActivity", "checkLocation: "+destination.toString());

        if(isAtDestination()){
            resetTimer();
            saveResults(manager);
            displayJourney();
        }
    }
    private boolean isAtDestination(){
        return destination.getLatitude()==latitude && destination.getLongitude()==longitude;
    }

    public void startTimer() {
        if(!isTimerRunning){
            chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffset);
            chronometer.start();
            isTimerRunning = true;
            chronometer.setVisibility(View.VISIBLE);
        }
    }
    private void setJourney(DataManager manager){
        try {
            start = manager.openPlace(fromHere.getSelectedItem().toString());
            destination = manager.openPlace(toHere.getSelectedItem().toString());
        }catch(Exception e){
            ModalDialogs.notifyException(MainActivity.this,e);
        }
    }

    private void displayJourney() {
        String journey = "From "+fromHere.getSelectedItem().toString()+" to "+toHere.getSelectedItem().toString();
        textView.setText(journey);
    }

    public void stopTimer(View view){
        if(isTimerRunning){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
           isTimerRunning =false;
        }
    }
    public void resetTimer(){
        if(isTimerRunning) {
            Log.i("MainActivity", "Time: "+chronometer.getBase());
            chronometerSeconds(chronometer);

            chronometer.stop();
            isTimerRunning = false;

        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;

    }

    private void saveResults(DataManager manager) {
        try {
            manager.savePath(new Path(start, destination, timeToDestination));
        }catch(Exception e){
            ModalDialogs.notifyException(MainActivity.this,e);
        }

    }

    private void displayResults() {
        String results ="Walked from "+fromHere.getSelectedItem().toString()+" to "+toHere.getSelectedItem().toString()+" in "+chronometer.getText();
        textView.setText(results);
    }

    public void openSettings() {
        Intent intent = new Intent(this,SettingsActivity1.class);
        startActivity(intent);
    }
    public int chronometerSeconds(Chronometer c){
        //credit to https://stackoverflow.com/questions/526524/android-get-time-of-chronometer-widget
        int seconds = 0;

        String chrono = c.getText().toString();
        String array[] = chrono.split(":");
        if (array.length == 2) {
            seconds = Integer.parseInt(array[0]) * 60  + Integer.parseInt(array[1]);
        } else if (array.length == 3) {
            seconds = Integer.parseInt(array[0]) * 60 * 60
                    + Integer.parseInt(array[1]) * 60
                    + Integer.parseInt(array[2]);
        }
        return seconds;
    }


}
