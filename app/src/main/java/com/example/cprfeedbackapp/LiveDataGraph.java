package com.example.cprfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


public class LiveDataGraph extends AppCompatActivity {

    protected GraphView graph;
    protected TextView textView;
    protected SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_data_graph);


        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        //Setting Up Graph
        graph = findViewById(R.id.liveGraph);
        textView = findViewById(R.id.nbCpr);


        textView.setText("Number of Compression: " + sharedPreferencesHelper.getCprNb() + "\n" + "Good Compressions: " + sharedPreferencesHelper.getGoodCprNb());
        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(15);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(100);
        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);


        //Line for saved data
        ArrayList<String> savedData = sharedPreferencesHelper.getEventSettingNames();
        if(!savedData.isEmpty()) {
            //Saved Data
            DataPoint[] dataPointArray = new DataPoint[savedData.size()];
            for (int dataPoint = 0; dataPoint < savedData.size(); dataPoint++) {
                DataPoint point = new DataPoint(dataPoint, Integer.parseInt(savedData.get(dataPoint)));
                dataPointArray[dataPoint] = point;
            }

            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPointArray);
            graph.addSeries(series2);

        }

        graphSetup();
    }


    // Define the axis and title of the graph
    protected void graphSetup()
    {
        graph.setTitle("Previous Session CPR");
        graph.setTitleTextSize((float)60);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time ms");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Compression");
    }

}