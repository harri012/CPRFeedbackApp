package com.example.cprfeedbackapp;


import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Set;

public class Bluetooth {
    //Declaring data members for bluetooth connection
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;
    private BluetoothSocket btSocket;
    private BluetoothServerSocket btServerSocket;
    private Set<BluetoothDevice> pairedDevices;
    private Context context;
    private Activity activity;
    protected HashMap<String, String> previouslyConnectedDevicesInfo;

    // Create a Broadcast receiver for ACTION.FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                pairedDevices.add(device);


                // TODO: Add Connect Code, Setup the recycler view to display the deviceInfo hashmap
            }
        }
    };

    public Bluetooth(Context aContext, Activity anActivity) {
        this.context  = aContext;
        this.btManager = context.getSystemService(BluetoothManager.class);
        this.btAdapter = btManager.getAdapter();
        this.activity = anActivity;
    }

    //Checks if bluetooth is enabled
    //Returns true if bluetooth is now enabled else returns false
    public boolean checkBluetoothEnabled(){
        //Returns null if it doesn't support Bluetooth
        if (btAdapter == null) {
            msg("Device Does Not Support Bluetooth!");
            return false;
        }
        else
        {
            //Prompts with dialog to activate bluetooth
            if (!btAdapter.isEnabled()) {
                if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.BLUETOOTH ) != PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(activity, new String [] { Manifest.permission.BLUETOOTH_CONNECT }, RequestCode.BLUETOOTH_PERMISSION);
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activity.startActivityForResult(enableBtIntent,  RequestCode.IMPORT);
                    if (!btAdapter.isEnabled())
                        return false;
                    else
                        return true;
                }
            }
        }
        return true;
    }




    //Search for discoverable devices
    private void findBluetoothDevice()
    {
        queryPairedDevice();

        // If there is a previously connected device, connect to it
        if (pairedDevices.size() > 0)
        {
            if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.BLUETOOTH ) == PackageManager.PERMISSION_GRANTED )
            {
                // Store the MAC address of the device for the connection phase
                for (BluetoothDevice device : pairedDevices) {
                    // Adds the name and MAC address of all devices to a Hashmap
                    previouslyConnectedDevicesInfo.put(device.getName(), device.getAddress());
                }

                // TODO: Add Connect Code, Setup the recycler view to display the deviceInfo hashmap
            }
        }

        // If no devices were connected previously, search for a new one
        else
            discoverDevices();
    }


    // Searches for nearby Bluetooth devices
    // Linked to a scan button
    public void discoverDevices ()
    {
        if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.BLUETOOTH ) == PackageManager.PERMISSION_GRANTED )
            btAdapter.startDiscovery();

        // Register for broadcast when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(receiver, filter);
    }


    // Unregister the ACTION_FOUND receiver
    public void unregisterDevice()
    {
        context.unregisterReceiver(receiver);
    }


    //Checks devices that are already paired
    public void queryPairedDevice(){

        if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.BLUETOOTH ) == PackageManager.PERMISSION_GRANTED )
            // Gets all the previously connected Bluetooth devices
            pairedDevices = btAdapter.getBondedDevices();

    }

    //Creates connection
    public void connectBluetoothDevice(){}


    //Input Stream of Bluetooth Data
    public String getInputData(){
        return "";
    }


    //Request Code Class
    private class RequestCode{
        static final int BLUETOOTH_PERMISSION = 100;
        static final int IMPORT = 101;
    }

    //For toasts
    private void msg(String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }


}




