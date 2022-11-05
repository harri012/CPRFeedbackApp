package com.example.cprfeedbackapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class CprPerformanceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    }

    // Define the axis and title of the graph
    protected void graphSetup()
    {
        graph.setTitle("Depth Recorded per Compression");
        graph.setTitleTextSize((float)60);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Depth");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_cpr_performance, container, false);

        graph = fragmentView.findViewById(R.id.graph);

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

        return fragmentView;
    }
}