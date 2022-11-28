package com.example.cprfeedbackapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link healthMonitoringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class healthMonitoringFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Python Integration Test Variable (to be deleted when test is done)
    protected TextView pythonTextView;

    public healthMonitoringFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment healthMonitoringFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static healthMonitoringFragment newInstance(String param1, String param2) {
        healthMonitoringFragment fragment = new healthMonitoringFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_health_monitoring, container, false);

        //Python Test (can be deleted later)
        pythonTextView = fragmentView.findViewById(R.id.pythonTextViewTest);

        //Python Integration Test
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this.getContext()));
        }

        Python py = Python.getInstance();
        PyObject pyobj = py.getModule("script");


        PyObject obj = pyobj.callAttr("main");

        pythonTextView.setText(obj.toString());

        return fragmentView;

    }
}