package com.example.gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.MapView;

public class MainActivity extends AppCompatActivity {

    private MapView mapDisp;
    private AutoCompleteTextView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapDisp = findViewById(R.id.map);
        search = findViewById(R.id.searchBar);


    }
}
