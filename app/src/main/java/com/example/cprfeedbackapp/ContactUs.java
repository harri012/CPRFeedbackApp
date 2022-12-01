package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity {

    protected TextView contactUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        setupHyperlink();
    }

    private void setupHyperlink() {
        contactUs = findViewById(R.id.aboutUsTextView);
        contactUs.setMovementMethod(LinkMovementMethod.getInstance());
        contactUs.setLinkTextColor(Color.BLUE);
    }
}