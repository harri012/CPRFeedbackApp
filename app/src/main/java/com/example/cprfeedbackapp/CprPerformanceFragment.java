package com.example.cprfeedbackapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class CprPerformanceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    protected SharedPreferencesHelper sharedPreferencesHelper;


    protected GraphView graph;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CprPerformanceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CprPerformanceFragment newInstance(String param1, String param2) {
        CprPerformanceFragment fragment = new CprPerformanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sharedPreferencesHelper = new SharedPreferencesHelper(this.getContext());
    }

    // Define the axis and title of the graph
    protected void graphSetup()
    {
        graph.setTitle("Data Received From Hardware");
        graph.setTitleTextSize((float)60);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Analog Value");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_cpr_performance, container, false);

        //Setting Up Graph
        graph = fragmentView.findViewById(R.id.graph);
        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(900);
        // activate horizontal scrolling
        graph.getViewport().setScrollable(true);

       /* //Line at 500
        DataPoint[] dataPointArray1 = new DataPoint[20];
        for(int dataPoint = 0; dataPoint < 20; dataPoint++)
        {
            DataPoint point = new DataPoint(dataPoint, 500);
            dataPointArray1[dataPoint] = point;
        }
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(dataPointArray1);
        graph.addSeries(series1);*/


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

        return fragmentView;
    }
}