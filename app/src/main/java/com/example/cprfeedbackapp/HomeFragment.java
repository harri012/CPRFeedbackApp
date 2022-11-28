package com.example.cprfeedbackapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private Button connectButton;
    private Button scanButton;
    private Button buttonGetData;

    protected RecyclerView recyclerView;
    protected List<DeviceInfoModel> deviceList;
    protected SharedPreferencesHelper sharedPreferencesHelper;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        // Gets the current theme of the app
        boolean isNightModeOn = sharedPreferencesHelper.getDarkModeState();

        // Sets the theme of the app depending on the value of isNightModeOn
        if (isNightModeOn)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        BluetoothServiceManager btServiceManager = new BluetoothServiceManager(HomeFragment.this.getContext(), HomeFragment.this.getActivity());

        Set<BluetoothDevice> pairedDevices = btServiceManager.queryPairedDevice();

        deviceList = new ArrayList<>();

        if (pairedDevices != null){

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    if (ActivityCompat.checkSelfPermission(HomeFragment.this.getContext(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {

                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress();

                        if (deviceName.equals("HC-05")) {
                            DeviceInfoModel deviceInfoModel = new DeviceInfoModel("CPR Feedback Device", deviceHardwareAddress);
                            deviceList.add(deviceInfoModel);
                        }


                        Log.e("Bt Service Data",deviceName);

                    }
                }

                //recyclerView = fragmentView.findViewById(R.id.deviceRecyclerView);
                recyclerView = fragmentView.findViewById(R.id.deviceRecyclerView);
                setupRecyclerView();
            }

            else
                Toast.makeText(HomeFragment.this.getContext(), "Pair a Bluetooth Device.", Toast.LENGTH_LONG).show();
        }

        else
            Toast.makeText(HomeFragment.this.getContext(), "Can't Display a device", Toast.LENGTH_LONG).show();

        return fragmentView;
    }

    public void setupRecyclerView() {

        // Display paired device using recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeFragment.this.getContext()));
        DeviceRecyclerViewAdapter deviceListAdapter = new DeviceRecyclerViewAdapter(deviceList, HomeFragment.this.getContext());
        recyclerView.setAdapter(deviceListAdapter);
    }


    //For toasts
    private void msg(String str) {
        Toast.makeText(HomeFragment.this.getContext(), str, Toast.LENGTH_LONG).show();
    }

}