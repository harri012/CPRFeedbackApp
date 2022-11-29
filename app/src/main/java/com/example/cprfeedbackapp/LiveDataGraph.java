package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LiveDataGraph extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data_graph);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}