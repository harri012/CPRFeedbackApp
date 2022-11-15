package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class dataActivity extends AppCompatActivity {
    protected TextView forceTextView;
    protected TextView depthTexView;

    private String deviceName = null;
    private String deviceAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Missing ConnectedThread Code here.
    }

    public void setup() {
        forceTextView = findViewById(R.id.forceDataTextView);
        depthTexView = findViewById(R.id.depthDataTextView);
    }
}