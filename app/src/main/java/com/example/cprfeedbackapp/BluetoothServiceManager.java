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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;


public class BluetoothServiceManager {
    //Declaring data members for bluetooth connection
    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice btDevice;
    private BluetoothSocket btSocket;
    private BluetoothServerSocket btServerSocket;
    private Set<BluetoothDevice> pairedDevices;
    private Context context;
    private Activity activity;


    // Create a Broadcast receiver for ACTION.FOUND
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                pairedDevices.add(device);

                Log.d("Devices found", pairedDevices.toString());
            }
        }
    };

    public BluetoothServiceManager(Context aContext, Activity anActivity) {
        this.context = aContext;
        this.btManager = context.getSystemService(BluetoothManager.class);
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.activity = anActivity;
    }

    //Checks if bluetooth is enabled
    //Returns true if bluetooth is now enabled else returns false
    public boolean checkBluetoothEnabled() {
        //Returns null if it doesn't support Bluetooth
        if (btAdapter == null) {
            msg("Device Does Not Support Bluetooth!");
            return false;
        } else {
            //Prompts with dialog to activate bluetooth
            if (!btAdapter.isEnabled()) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, RequestCode.BLUETOOTH_PERMISSION);
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    activity.startActivityForResult(enableBtIntent, RequestCode.IMPORT);
                    if (!btAdapter.isEnabled())
                        return false;
                    else
                        return true;
                }
            }
        }

        Log.i("Bt Service Manager", "Bluetooth On");
        return true;
    }


    public BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }




    //Checks devices that are already paired
    public Set<BluetoothDevice> queryPairedDevice() {

        // Gets all the previously connected Bluetooth devices
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
        pairedDevices = btAdapter.getBondedDevices();
        }
        else {
            msg("no permission");
        }

        return pairedDevices;
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




