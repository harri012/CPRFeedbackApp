package com.example.cprfeedbackapp;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

public class Bluetooth {
    //Declaring data members for bluetooth connection
    BluetoothAdapter btAdapter;
    BluetoothDevice btDevice;
    BluetoothSocket btSocket;
    BluetoothServerSocket btServerSocket;

    //Checks if bluetooth is enabled
    private boolean checkBluetoothEnabled(){
        return false;
    }

    //Search for discoverable devices
    private void findBluetoothDevice(){}

    //Checks devices that are already paired
    private void queryPairedDevice(){}

    //Creates connection
    private void connectBluetoothDevice(){}


    //Input Stream of Bluetooth Data
    private String getInputData(){
        return "";
    }
}


