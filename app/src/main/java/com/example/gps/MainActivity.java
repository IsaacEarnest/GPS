package com.example.gps;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Button addLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView textView;
    private EditText newLocationName;
    private Spinner fromHere;
    private Spinner toHere;

    private long timeToDestination;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    private List<String> list;

    private File internalFileDir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        chronometer = findViewById(R.id.chronometer);
        addLocation = findViewById(R.id.addLocation);
        newLocationName = findViewById(R.id.newLocationName);
        fromHere = findViewById(R.id.fromHere);
        toHere = findViewById(R.id.toHere);
        list = new ArrayList<>();
        list.add("list 1");
        running = false;

        internalFileDir = getFilesDir();

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
                addPlace();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                checkLocation(location);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }
    public class place{
        String name;
        double latitude;
        double longitude;
        place(){}
        place(String name, double latitude, double longitude){
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private void addPlace() {
        String value = newLocationName.getText().toString().trim();
        if (!value.equals("")) {
            String addLocationSuccess = " has been Added!";
            list.add(value);
            place newPlace = new place(value,);
            resetAddLocation();
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
    private void checkLocation(Location location){
        //Log.i("MainActivity", "checkLocation: " + prefs);
        if(isAtDestination(location)){
           String direction = ""+fromHere.getSelectedItem().toString()+toHere.getSelectedItem().toString();
           String time = Long.toString(timeToDestination);
           // prefs.edit().putString(direction, time).apply();
        }
    }
    private boolean isAtDestination(Location location){
        double locationLongitude =location.getLongitude();
        double locationLatitude = location.getLatitude();

        //TODO check if coordinates are equal to destination
    return false;
    }

    public void startTimer(View view) {
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime()- pauseOffset);
            chronometer.start();
            running = true;
            chronometer.setVisibility(View.VISIBLE);
            displayJourney();
        }
    }

    private void displayJourney() {
        textView.setText("From "+fromHere.getSelectedItem().toString()+" to "+toHere.getSelectedItem().toString());
    }

    public void stopTimer(View view){
        if(running){
            timeToDestination = chronometer.getBase();
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
           running=false;
        }
    }
    public void resetTimer(View view){
        if(running) {
            chronometer.stop();
            running = false;
            displayResults();
            saveResults();
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;

    }

    private void saveResults() {
        //TODO save time and journey together

    }

    private void displayResults() {
        textView.setText("Walked from "+fromHere.getSelectedItem().toString()+" to "+toHere.getSelectedItem().toString()+" in "+chronometer.getText());
    }

    public void openSettings(View view) {

    }

/*    private String readSomething() {
        try {
            File inputFile = new File(internalFileDir, "newFile.txt");
            Scanner s = new Scanner(inputFile);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                Log.i("MainActivity", "Input line: " + line);
            }
            s.close();
        } catch (FileNotFoundException e) {
            Log.e("MainActivity", "File not found");
        }
    }

    private void saveSomething() {
        try {
            File newFile = new File(internalFileDir, "newFile.txt");
            PrintWriter fileOut = new PrintWriter(new FileWriter(newFile));
            fileOut.println("This is some output text");
            fileOut.close();
        } catch (IOException e) {
            Log.e("MainActivity", "We had an exception: " + e);
        }
    }
    */
}
