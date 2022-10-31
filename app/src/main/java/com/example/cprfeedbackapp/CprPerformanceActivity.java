package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class CprPerformanceActivity extends AppCompatActivity {

    //Declaring graph views and text views
    protected GraphView cprGraph;
    protected TextView depthTextView, forceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpr_performance);



        setupUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupUI();
    }


    protected void graphSetup()
    {
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 2),
                new DataPoint(1, 10),
                new DataPoint(2, 6),
                new DataPoint(3, 4),
                new DataPoint(4, 12)
        });

        cprGraph.addSeries(series1);
        cprGraph.addSeries(series2);
        cprGraph.setTitle("Live Depth Recorded per Compression");
        cprGraph.setTitleTextSize((float)50);
        cprGraph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        cprGraph.getGridLabelRenderer().setVerticalAxisTitle("Depth");
    }

    protected void setupUI()
    {
        // Up Navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cprGraph = findViewById(R.id.graph);
        depthTextView = findViewById(R.id.depthTextView);
        forceTextView = findViewById(R.id.forceTextView);

        graphSetup();
    }
}