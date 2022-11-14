package com.example.cprfeedbackapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private Button connectButton;
    private Button scanButton;
    private Button buttonGetData;

    protected RecyclerView recyclerView;
    protected List<DeviceInfoModel> deviceList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
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
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public void onResume() {
        super.onResume();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);

//      BluetoothServiceManager bluetoothServiceManager = new BluetoothServiceManager(SettingsFragment.this.getContext(), SettingsFragment.this.getActivity());

//        scanButton = fragmentView.findViewById(R.id.scanButton);
//
//        scanButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bluetoothServiceManager.discoverDevices();
//            }
//        });
//
//        connectButton = fragmentView.findViewById(R.id.connectButton);
//
//        connectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bluetoothServiceManager.connectBluetoothDevice();
//            }
//        });
//
//        buttonGetData = fragmentView.findViewById(R.id.buttonDisplayData);
//        buttonGetData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BluetoothServiceManager bt = new BluetoothServiceManager(SettingsFragment.this.getContext(), SettingsFragment.this.getActivity());
//                if(bt.checkBluetoothEnabled())
//                {
//                    bt.getInputData();
//                }
//
//            }
//        });

        Set<BluetoothDevice> pairedDevices = new BluetoothServiceManager(SettingsFragment.this.getContext(),
                                        SettingsFragment.this.getActivity()).queryPairedDevice();

        deviceList = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {

                if (ActivityCompat.checkSelfPermission(SettingsFragment.this.getContext(),
                        Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress();
                    DeviceInfoModel deviceInfoModel = new DeviceInfoModel(deviceName, deviceHardwareAddress);
                    deviceList.add(deviceInfoModel);
                }
            }

            recyclerView = fragmentView.findViewById(R.id.deviceRecyclerView);
            setupRecyclerView();
        }

        else
            Toast.makeText(SettingsFragment.this.getContext(), "Pair a Bluetooth Device.", Toast.LENGTH_LONG).show();

        return fragmentView;
    }

    public void setupRecyclerView() {

        // Display paired device using recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(SettingsFragment.this.getContext()));
        DeviceRecyclerViewAdapter deviceListAdapter = new DeviceRecyclerViewAdapter(deviceList, SettingsFragment.this.getContext());
        recyclerView.setAdapter(deviceListAdapter);
    }
}