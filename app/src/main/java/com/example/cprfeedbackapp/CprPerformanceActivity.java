package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class CprPerformanceActivity extends AppCompatActivity {

    protected GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpr_performance);

        // Up Navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        graph.addSeries(series1);
        graph.addSeries(series2);

        graphSetup();
    }

    protected void graphSetup()
    {
        graph.setTitle("Live Depth Recorded per Compression");
        graph.setTitleTextSize((float)60);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Depth");
    }

    protected void setup()
    {
        graph = findViewById(R.id.graph);
    }
}