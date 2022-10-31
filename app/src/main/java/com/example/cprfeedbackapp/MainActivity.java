package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected Button cprButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        cprButton.setOnClickListener(view -> goToCprPerformance());
    }

    protected void setup()
    {
        cprButton = findViewById(R.id.cprButton);
    }

    protected void goToCprPerformance()
    {
        Intent intent = new Intent(this, CprPerformanceActivity.class);
        startActivity(intent);
    }
}