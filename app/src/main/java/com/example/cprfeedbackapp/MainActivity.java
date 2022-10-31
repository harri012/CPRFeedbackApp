package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Declaring buttons
    protected Button cprButton, settingsButton, healthMonitorButton, cprTutorialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupUI();

    }


    protected void setupUI()
    {
        //Setting up buttons
        cprButton = findViewById(R.id.cprButton);
        settingsButton = findViewById(R.id.settingsButton);
        healthMonitorButton = findViewById(R.id.healthButton);
        cprTutorialButton = findViewById(R.id.tutorialButton);

        //On click listeners to go to activities
        cprButton.setOnClickListener(view -> goToCprPerformance());
        settingsButton.setOnClickListener(view -> goToSettings());
        healthMonitorButton.setOnClickListener(view -> goToHealthMonitor());
        cprTutorialButton.setOnClickListener(view -> goToCPRTutorial());
    }



    //GO to Activity Methods
    protected void goToCprPerformance()
    {
        Intent intent = new Intent(this, CprPerformanceActivity.class);
        startActivity(intent);
    }

    protected void goToSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    protected void goToHealthMonitor()
    {
        Intent intent = new Intent(this, HealthMonitorActivity.class);
        startActivity(intent);
    }

    protected void goToCPRTutorial()
    {
        Intent intent = new Intent(this, CPRTutorialActivity.class);
        startActivity(intent);
    }
}