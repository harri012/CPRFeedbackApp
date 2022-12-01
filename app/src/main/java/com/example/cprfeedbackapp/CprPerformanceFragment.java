package com.example.cprfeedbackapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cprfeedbackapp.database.AppDatabase;
import com.example.cprfeedbackapp.database.entity.AverageDepthForce;
import com.example.cprfeedbackapp.database.entity.WaveformForce;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
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
    protected AppDatabase db;

    protected List<Double> averageDepthList = new ArrayList<>();
    protected List<Double> averageForceList = new ArrayList<>();
    protected List<Double> waveformForcesList = new ArrayList<>();

    protected Button plotNextButton;
    protected int plotIndex = 2;

    DataPoint[] avgDepth, avgForce, waveForce;

    protected GraphView graph;
    protected CprSessionRecyclerViewAdapter CprSessionRecyclerViewAdapter;
    protected RecyclerView CprSessionRecyclerView;

    protected List<String> sessionDateList = new ArrayList<>();

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
        graph.setTitleTextSize((float)40);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_cpr_performance, container, false);

        // Instantiating a database objext
        db = AppDatabase.getInstance(this.getContext());

        //Setting Up Graph
        graph = fragmentView.findViewById(R.id.graph);

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setMinY(0);
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


//        //Line for saved data
//        ArrayList<String> savedData = sharedPreferencesHelper.getEventSettingNames();
//        if(!savedData.isEmpty()) {
//            //Saved Data
//            DataPoint[] dataPointArray = new DataPoint[savedData.size()];
//            for (int dataPoint = 0; dataPoint < savedData.size(); dataPoint++) {
//                DataPoint point = new DataPoint(dataPoint, Integer.parseInt(savedData.get(dataPoint)));
//                dataPointArray[dataPoint] = point;
//            }
//
//            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPointArray);
//            graph.addSeries(series2);
//        }

        CprSessionRecyclerView = fragmentView.findViewById(R.id.CprSessionRecyclerView);
        setupRecyclerView();

        // Get the data from database and store them in lists
        for(AverageDepthForce averageDepthForce : CprSessionRecyclerViewAdapter.getSessionAverageDatapoints()) {
            averageDepthList.add(averageDepthForce.getDepthDatapoint());
            averageForceList.add(averageDepthForce.getForceDatapoint());
        }

        for(WaveformForce waveformForce : CprSessionRecyclerViewAdapter.getSessionWaveformForce()) {
            waveformForcesList.add(waveformForce.getForceDatapoint());
        }

//        averageDepthList.add(5.20);
//        averageDepthList.add(2.51);
//        averageDepthList.add(3.66);
//
//        averageForceList.add(5.22);
//        averageForceList.add(2.98);
//        averageForceList.add(1.32);
//
//        waveformForcesList.add(8.52);
//        waveformForcesList.add(8.52);
//        waveformForcesList.add(8.52);



        // Get the data in an Array
        avgDepth = getAverageDepthArray();
        avgForce = getAverageForceArray();
        waveForce = getWaveformForceArray();

        plotNextButton = fragmentView.findViewById(R.id.plotNextButton);
        plotNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (plotIndex) {

                    // Plotting Average Force as a Bar Graph
                    case 0:
                        plotIndex = 1;

                        // Clearing the Graph
                        graph.removeAllSeries();

                        // Settings the axis titles
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Compression");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Force Applied (N)");

                        // Plotting the graph
                        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<>(avgForce);
                        graph.addSeries(series1);
                        series1.setSpacing(50);
                        graph.setTitle("Average Force Measured Per Compressions");


                        break;

                    // Plotting Waveform Force Data as a Line Graph
                    case 1:
                        plotIndex = 2;

                        graph.removeAllSeries();

                        // Settings the axis titles
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time (ms)");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Force Applied (N)");

                        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(waveForce);
                        graph.addSeries(series3);

                        graph.setTitle("Change in Force Measured Per Compressions");

                        break;

                    // Plotting Average Depth as a Bar Graph
                    case 2:
                        plotIndex = 0;

                        graph.removeAllSeries();

                        // Settings the axis titles
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Compression");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Depth (cm)");

                        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(avgDepth);
                        graph.addSeries(series2);
                        series2.setSpacing(50);
                        graph.setTitle("Average Depth Measured Per Compressions");

                        break;
                }
            }
        });

        return fragmentView;
    }

    protected void setupRecyclerView() {

//        sessionDateList = db.averageDepthForceDao().getAllUniqueDates();
//        sessionDateList.add("aaa");
//        sessionDateList.add("bbb");
//        sessionDateList.add("ccc");
//        sessionDateList.add("ddd");
        CprSessionRecyclerViewAdapter = new CprSessionRecyclerViewAdapter(sessionDateList);
        CprSessionRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        CprSessionRecyclerView.setAdapter(CprSessionRecyclerViewAdapter);
    }

    // Create DataPoint [] from the list obtained from the adapter
    public DataPoint[] getAverageDepthArray() {

        DataPoint[] averageForceDatapointArray = new DataPoint[averageDepthList.size()];
        for(int i = 0; i < averageDepthList.size(); i++) {
            DataPoint point = new DataPoint((int)(i + 1), averageDepthList.get(i));
            Log.i("x-coord", String.valueOf(point.getX()));
            Log.i("x-coord", String.valueOf(point.getY()));
            averageForceDatapointArray[i] = point;
        }

        return averageForceDatapointArray;
    }

    public DataPoint[] getAverageForceArray() {

        DataPoint[] averageForceDatapointArray = new DataPoint[averageForceList.size()];
        for(int i = 0; i < averageForceList.size(); i++) {
            DataPoint point = new DataPoint((int)(i + 1), averageForceList.get(i));

            averageForceDatapointArray[i] = point;
        }

        return averageForceDatapointArray;
    }

    public DataPoint[] getWaveformForceArray() {

        DataPoint[] waveformForceDatapointArray = new DataPoint[waveformForcesList.size()];
        for(int i = 0; i < waveformForcesList.size(); i++) {
            DataPoint point = new DataPoint((0.1 * (i + 1)), waveformForcesList.get(i));

            waveformForceDatapointArray[i] = point;
        }

        return waveformForceDatapointArray;
    }
}