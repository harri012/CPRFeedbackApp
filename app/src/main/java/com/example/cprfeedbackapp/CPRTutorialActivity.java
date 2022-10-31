package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CPRTutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cprtutorial);

        // Up Navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}