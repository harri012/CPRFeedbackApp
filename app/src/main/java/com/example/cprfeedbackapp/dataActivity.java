package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

// Test activity for displaying the data
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


    }

    public void setup() {
        forceTextView = findViewById(R.id.forceDataTextView);
        depthTexView = findViewById(R.id.depthDataTextView);
    }
}